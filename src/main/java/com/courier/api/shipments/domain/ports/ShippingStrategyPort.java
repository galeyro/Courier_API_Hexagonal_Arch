package com.courier.api.shipments.domain.ports;

import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;

import java.math.BigDecimal;

public interface ShippingStrategyPort {

    ShipmentType supportedType();

    void validate(Shipment shipment);

    BigDecimal calculateCost(Shipment shipment);

    ShipmentStatus execute(Shipment shipment);
}
