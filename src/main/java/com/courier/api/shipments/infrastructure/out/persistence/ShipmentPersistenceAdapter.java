package com.courier.api.shipments.infrastructure.out.persistence;

import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.ports.ShipmentRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ShipmentPersistenceAdapter implements ShipmentRepositoryPort {

    private final ShipmentJpaRepository shipmentJpaRepository;
    private final ShipmentEntityMapper mapper;

    public ShipmentPersistenceAdapter(ShipmentJpaRepository shipmentJpaRepository, ObjectMapper objectMapper) {
        this.shipmentJpaRepository = shipmentJpaRepository;
        this.mapper = new ShipmentEntityMapper(objectMapper);
    }

    @Override
    public Shipment save(Shipment shipment) {
        return mapper.toDomain(shipmentJpaRepository.save(mapper.toEntity(shipment)));
    }

    @Override
    public Optional<Shipment> findById(UUID id) {
        return shipmentJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Shipment> findByCustomerId(UUID customerId) {
        return shipmentJpaRepository.findBySenderIdOrRecipientIdOrderByCreatedAtDesc(customerId, customerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
