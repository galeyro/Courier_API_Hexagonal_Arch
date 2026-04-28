package com.courier.api.shipments.application.strategy;

import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import com.courier.api.shipments.domain.ports.ShippingStrategyPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StandardShippingStrategy extends AbstractShippingStrategy implements ShippingStrategyPort {

    private static final BigDecimal MINIMUM_COST = new BigDecimal("5000");

    @Override
    public ShipmentType supportedType() {
        return ShipmentType.STANDARD;
    }

    @Override
    public void validate(Shipment shipment) {
        ensureDifferentParties(shipment);
        BigDecimal weight = decimalValue(shipment.getMetadata().get("weightKg"), "weightKg");
        if (weight.compareTo(new BigDecimal("20")) > 0) {
            throw new InvalidShipmentException("STANDARD shipment weight must be <= 20 kg");
        }
    }

    @Override
    public BigDecimal calculateCost(Shipment shipment) {
        BigDecimal percentageCost = shipment.getDeclaredValue()
                .multiply(new BigDecimal("0.001"))
                .setScale(2, RoundingMode.HALF_UP);
        return percentageCost.max(MINIMUM_COST);
    }

    @Override
    public ShipmentStatus execute(Shipment shipment) {
        return ShipmentStatus.DELIVERED;
    }
}
