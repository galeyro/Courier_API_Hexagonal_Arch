package com.courier.api.shipments.domain.ports;

import com.courier.api.shipments.domain.model.Shipment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port for shipment persistence operations.
 */
public interface ShipmentRepositoryPort {

    /**
     * Saves a shipment.
     */
    Shipment save(Shipment shipment);

    /**
     * Finds a shipment by ID.
     */
    Optional<Shipment> findById(UUID id);

    /**
     * Finds shipments by customer ID.
     */
    List<Shipment> findByCustomerId(UUID customerId);
}
