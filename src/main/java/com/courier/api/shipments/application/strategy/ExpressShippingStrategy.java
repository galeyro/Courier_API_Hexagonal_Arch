package com.courier.api.shipments.application.strategy;

import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import com.courier.api.shipments.domain.ports.ShippingStrategyPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExpressShippingStrategy extends AbstractShippingStrategy implements ShippingStrategyPort {

    private static final BigDecimal FIXED_COST = new BigDecimal("15000");
    private static final BigDecimal MAX_DECLARED_VALUE = new BigDecimal("3000000");

    @Override
    public ShipmentType supportedType() {
        return ShipmentType.EXPRESS;
    }

    @Override
    public void validate(Shipment shipment) {
        BigDecimal weight = decimalValue(shipment.getMetadata().get("weightKg"), "weightKg");
        if (weight.compareTo(new BigDecimal("5")) > 0) {
            throw new InvalidShipmentException("EXPRESS shipment weight must be <= 5 kg");
        }
        if (shipment.getDeclaredValue().compareTo(MAX_DECLARED_VALUE) > 0) {
            throw new InvalidShipmentException("EXPRESS shipment declaredValue must be <= 3000000");
        }
    }

    @Override
    public BigDecimal calculateCost(Shipment shipment) {
        return FIXED_COST;
    }

    @Override
    public ShipmentStatus execute(Shipment shipment) {
        return ShipmentStatus.DELIVERED;
    }
}
