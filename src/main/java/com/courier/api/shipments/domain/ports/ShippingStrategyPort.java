package com.courier.api.shipments.domain.ports;

import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import java.math.BigDecimal;

/**
 * Port for shipping strategy operations.
 */
public interface ShippingStrategyPort {

    /**
     * Returns the shipment type supported by this strategy.
     */
    ShipmentType supportedType();

    /**
     * Validates a shipment against strategy rules.
     */
    void validate(Shipment shipment);

    /**
     * Calculates the shipping cost for a shipment.
     */
    BigDecimal calculateCost(Shipment shipment);

    /**
     * Executes the shipping strategy and returns the resulting status.
     */
    ShipmentStatus execute(Shipment shipment);
}
