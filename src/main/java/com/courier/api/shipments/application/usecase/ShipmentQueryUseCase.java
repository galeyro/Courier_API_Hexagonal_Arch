package com.courier.api.shipments.application.usecase;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.exception.ShipmentNotFoundException;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for shipment read operations.
 */
@Service
public class ShipmentQueryUseCase {

    private final ShipmentRepositoryPort shipmentRepository;

    /**
     * Constructs a new ShipmentQueryUseCase.
     */
    public ShipmentQueryUseCase(ShipmentRepositoryPort shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * Finds a shipment by ID.
     */
    @Transactional(readOnly = true)
    public ShipmentResponse findById(UUID id) {
        return shipmentRepository.findById(id)
                .map(ShipmentMapper::toResponse)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + id));
    }

    /**
     * Finds shipments by customer ID.
     */
    @Transactional(readOnly = true)
    public List<ShipmentResponse> findByCustomerId(UUID customerId) {
        return shipmentRepository.findByCustomerId(customerId).stream()
                .map(ShipmentMapper::toResponse)
                .toList();
    }
}
