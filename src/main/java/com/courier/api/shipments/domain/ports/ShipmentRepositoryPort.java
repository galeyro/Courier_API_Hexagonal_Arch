package com.courier.api.shipments.domain.ports;

import com.courier.api.shipments.domain.model.Shipment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepositoryPort {

    Shipment save(Shipment shipment);

    Optional<Shipment> findById(UUID id);

    List<Shipment> findByCustomerId(UUID customerId);
}
