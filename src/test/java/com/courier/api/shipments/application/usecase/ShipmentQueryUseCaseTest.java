package com.courier.api.shipments.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.exception.ShipmentNotFoundException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
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
class ShipmentQueryUseCaseTest {

    @Mock
    private ShipmentRepositoryPort shipmentRepository;

    private ShipmentQueryUseCase shipmentQueryUseCase;

    @BeforeEach
    void setUp() {
        shipmentQueryUseCase = new ShipmentQueryUseCase(shipmentRepository);
    }

    @Test
    void findById_ShouldReturnShipment_WhenShipmentExists() {
        UUID shipmentId = UUID.randomUUID();
        Shipment shipment = new Shipment(
                shipmentId, UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), new BigDecimal("5000"),
                ShipmentType.STANDARD, ShipmentStatus.DELIVERED, Map.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));

        ShipmentResponse response = shipmentQueryUseCase.findById(shipmentId);

        assertNotNull(response);
        assertEquals(shipmentId, response.id());
        assertEquals(new BigDecimal("5000"), response.shippingCost());
    }

    @Test
    void findById_ShouldThrowException_WhenShipmentDoesNotExist() {
        UUID shipmentId = UUID.randomUUID();

        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class, () -> shipmentQueryUseCase.findById(shipmentId));
    }

    @Test
    void findByCustomerId_ShouldReturnList() {
        UUID customerId = UUID.randomUUID();
        Shipment shipment = new Shipment(
                UUID.randomUUID(), customerId, UUID.randomUUID(), new BigDecimal("100000"), new BigDecimal("5000"),
                ShipmentType.STANDARD, ShipmentStatus.DELIVERED, Map.of(), LocalDateTime.now(), LocalDateTime.now()
        );

        when(shipmentRepository.findByCustomerId(customerId)).thenReturn(List.of(shipment));

        List<ShipmentResponse> responses = shipmentQueryUseCase.findByCustomerId(customerId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(customerId, responses.get(0).senderId());
    }
}
