package com.courier.api.shipments.application.usecase;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.exception.ShipmentNotFoundException;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShipmentQueryUseCase {

    private final ShipmentRepositoryPort shipmentRepository;

    public ShipmentQueryUseCase(ShipmentRepositoryPort shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional(readOnly = true)
    public ShipmentResponse findById(UUID id) {
        return shipmentRepository.findById(id)
                .map(ShipmentMapper::toResponse)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ShipmentResponse> findByCustomerId(UUID customerId) {
        return shipmentRepository.findByCustomerId(customerId).stream()
                .map(ShipmentMapper::toResponse)
                .toList();
    }
}
