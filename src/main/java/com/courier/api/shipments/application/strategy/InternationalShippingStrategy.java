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
public class InternationalShippingStrategy extends AbstractShippingStrategy implements ShippingStrategyPort {

    private static final BigDecimal BASE_COST = new BigDecimal("50000");
    private static final BigDecimal MAX_DECLARED_VALUE = new BigDecimal("50000000");

    @Override
    public ShipmentType supportedType() {
        return ShipmentType.INTERNATIONAL;
    }

    @Override
    public void validate(Shipment shipment) {
        requireMetadataField(shipment.getMetadata(), "destinationCountry");
        requireMetadataField(shipment.getMetadata(), "customsDeclaration");
        if (shipment.getDeclaredValue().compareTo(MAX_DECLARED_VALUE) > 0) {
            throw new InvalidShipmentException("INTERNATIONAL shipment declaredValue must be <= 50000000");
        }
    }

    @Override
    public BigDecimal calculateCost(Shipment shipment) {
        BigDecimal variableCost = shipment.getDeclaredValue()
                .multiply(new BigDecimal("0.02"))
                .setScale(2, RoundingMode.HALF_UP);
        return BASE_COST.add(variableCost);
    }

    @Override
    public ShipmentStatus execute(Shipment shipment) {
        return ShipmentStatus.IN_CUSTOMS;
    }
}
