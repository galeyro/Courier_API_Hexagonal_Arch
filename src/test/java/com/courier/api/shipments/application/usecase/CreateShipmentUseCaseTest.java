package com.courier.api.shipments.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.model.CustomerRole;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import com.courier.api.shared.events.domain.model.EventTopics;
import com.courier.api.shared.events.domain.model.ShipmentEvent;
import com.courier.api.shared.events.domain.ports.EventPublisher;
import com.courier.api.shipments.application.dto.CreateShipmentRequest;
import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
import com.courier.api.shipments.domain.ports.ShippingStrategyPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateShipmentUseCaseTest {

    @Mock
    private ShipmentRepositoryPort shipmentRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ShippingStrategyPort standardStrategy;

    private CreateShipmentUseCase createShipmentUseCase;

    @BeforeEach
    void setUp() {
        when(standardStrategy.supportedType()).thenReturn(ShipmentType.STANDARD);
        createShipmentUseCase = new CreateShipmentUseCase(
                shipmentRepository,
                customerRepository,
                eventPublisher,
                List.of(standardStrategy)
        );
    }

    @Test
    void create_ShouldCreateShipmentSuccessfully_WhenValidRequest() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());

        Customer sender = new Customer(senderId, "Sender", "sender@example.com", "password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());
        Customer recipient = new Customer(recipientId, "Recipient", "recipient@example.com", "password", CustomerRole.ADMIN, true, LocalDateTime.now(), LocalDateTime.now());

        Shipment savedShipment = new Shipment(
                UUID.randomUUID(), senderId, recipientId, new BigDecimal("100000"), new BigDecimal("5000"),
                ShipmentType.STANDARD, ShipmentStatus.DELIVERED, Map.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(customerRepository.findActiveById(senderId)).thenReturn(Optional.of(sender));
        when(customerRepository.findActiveById(recipientId)).thenReturn(Optional.of(recipient));
        when(standardStrategy.calculateCost(any(Shipment.class))).thenReturn(new BigDecimal("5000"));
        when(standardStrategy.execute(any(Shipment.class))).thenReturn(ShipmentStatus.DELIVERED);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);

        ShipmentResponse response = createShipmentUseCase.create(request);

        assertNotNull(response);
        assertEquals(savedShipment.getId(), response.id());
        assertEquals(new BigDecimal("5000"), response.shippingCost());
        assertEquals(ShipmentStatus.DELIVERED, response.status());

        verify(standardStrategy).validate(any(Shipment.class));
        verify(shipmentRepository).save(any(Shipment.class));
        verify(eventPublisher).publish(eq(EventTopics.SHIPMENT_DISPATCHED), any(ShipmentEvent.class));
    }

    @Test
    void create_ShouldThrowException_WhenSenderAndRecipientAreSame() {
        UUID senderId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, senderId, new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());

        assertThrows(InvalidShipmentException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }

    @Test
    void create_ShouldThrowException_WhenDeclaredValueIsZeroOrNegative() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, BigDecimal.ZERO, ShipmentType.STANDARD, Map.of());

        assertThrows(InvalidShipmentException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }

    @Test
    void create_ShouldThrowException_WhenSenderNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());

        when(customerRepository.findActiveById(senderId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }

    @Test
    void create_ShouldThrowException_WhenRecipientNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());

        Customer sender = new Customer(senderId, "Sender", "sender@example.com", "password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findActiveById(senderId)).thenReturn(Optional.of(sender));
        when(customerRepository.findActiveById(recipientId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }

    @Test
    void create_ShouldThrowException_WhenSenderOrRecipientIsInactive() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());

        Customer sender = new Customer(senderId, "Sender", "sender@example.com", "password", CustomerRole.SENDER, false, LocalDateTime.now(), LocalDateTime.now());
        Customer recipient = new Customer(recipientId, "Recipient", "recipient@example.com", "password", CustomerRole.ADMIN, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findActiveById(senderId)).thenReturn(Optional.of(sender));
        when(customerRepository.findActiveById(recipientId)).thenReturn(Optional.of(recipient));

        assertThrows(InvalidShipmentException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }

    @Test
    void create_ShouldThrowException_WhenStrategyNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(senderId, recipientId, new BigDecimal("100000"), ShipmentType.EXPRESS, Map.of());

        Customer sender = new Customer(senderId, "Sender", "sender@example.com", "password", CustomerRole.SENDER, true, LocalDateTime.now(), LocalDateTime.now());
        Customer recipient = new Customer(recipientId, "Recipient", "recipient@example.com", "password", CustomerRole.ADMIN, true, LocalDateTime.now(), LocalDateTime.now());

        when(customerRepository.findActiveById(senderId)).thenReturn(Optional.of(sender));
        when(customerRepository.findActiveById(recipientId)).thenReturn(Optional.of(recipient));

        assertThrows(InvalidShipmentException.class, () -> createShipmentUseCase.create(request));
        verifyNoInteractions(shipmentRepository, eventPublisher);
    }
}
