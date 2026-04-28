package com.courier.api.shipments.application.usecase;

import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import com.courier.api.shared.events.domain.model.EventTopics;
import com.courier.api.shared.events.domain.model.ShipmentEvent;
import com.courier.api.shared.events.domain.ports.EventPublisher;
import com.courier.api.shipments.application.dto.CreateShipmentRequest;
import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
import com.courier.api.shipments.domain.ports.ShippingStrategyPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CreateShipmentUseCase {

    private final ShipmentRepositoryPort shipmentRepository;
    private final CustomerRepositoryPort customerRepository;
    private final EventPublisher eventPublisher;
    private final Map<com.courier.api.shipments.domain.model.ShipmentType, ShippingStrategyPort> strategies;

    public CreateShipmentUseCase(
            ShipmentRepositoryPort shipmentRepository,
            CustomerRepositoryPort customerRepository,
            EventPublisher eventPublisher,
            List<ShippingStrategyPort> strategies
    ) {
        this.shipmentRepository = shipmentRepository;
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
        this.strategies = strategies.stream().collect(Collectors.toMap(ShippingStrategyPort::supportedType, Function.identity()));
    }

    @Transactional
    public ShipmentResponse create(CreateShipmentRequest request) {
        validateCommonRules(request.senderId(), request.recipientId(), request.declaredValue());

        Shipment shipment = Shipment.pending(
                request.senderId(),
                request.recipientId(),
                request.declaredValue(),
                request.type(),
                request.metadata()
        );

        ShippingStrategyPort strategy = strategies.get(request.type());
        if (strategy == null) {
            throw new InvalidShipmentException("No strategy registered for shipment type: " + request.type());
        }

        strategy.validate(shipment);
        ShipmentStatus status = strategy.execute(shipment);
        Shipment processedShipment = shipment.processed(strategy.calculateCost(shipment), status);
        Shipment savedShipment = shipmentRepository.save(processedShipment);

        eventPublisher.publish(resolveTopic(savedShipment.getStatus()), new ShipmentEvent(
                savedShipment.getId(),
                savedShipment.getSenderId(),
                savedShipment.getRecipientId(),
                savedShipment.getDeclaredValue(),
                savedShipment.getShippingCost(),
                savedShipment.getType(),
                savedShipment.getStatus(),
                savedShipment.getUpdatedAt()
        ));

        return ShipmentMapper.toResponse(savedShipment);
    }

    private void validateCommonRules(UUID senderId, UUID recipientId, java.math.BigDecimal declaredValue) {
        if (senderId.equals(recipientId)) {
            throw new InvalidShipmentException("Sender and recipient must be different customers");
        }
        if (declaredValue == null || declaredValue.signum() <= 0) {
            throw new InvalidShipmentException("Declared value must be greater than zero");
        }

        Customer sender = customerRepository.findActiveById(senderId)
                .orElseThrow(() -> new CustomerNotFoundException("Active sender not found: " + senderId));
        Customer recipient = customerRepository.findActiveById(recipientId)
                .orElseThrow(() -> new CustomerNotFoundException("Active recipient not found: " + recipientId));

        if (!sender.isActive() || !recipient.isActive()) {
            throw new InvalidShipmentException("Both sender and recipient must be active");
        }
    }

    private String resolveTopic(ShipmentStatus status) {
        return switch (status) {
            case DELIVERED -> EventTopics.SHIPMENT_DISPATCHED;
            case IN_CUSTOMS -> EventTopics.SHIPMENT_IN_CUSTOMS;
            case FAILED -> EventTopics.SHIPMENT_FAILED;
            case PENDING -> throw new InvalidShipmentException("Pending shipments cannot be published");
        };
    }
}
