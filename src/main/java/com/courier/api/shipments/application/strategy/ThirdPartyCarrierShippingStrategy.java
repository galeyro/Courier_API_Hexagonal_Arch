package com.courier.api.shipments.application.strategy;

import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import com.courier.api.shipments.domain.ports.ShippingStrategyPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ThirdPartyCarrierShippingStrategy extends AbstractShippingStrategy implements ShippingStrategyPort {

    @Override
    public ShipmentType supportedType() {
        return ShipmentType.THIRD_PARTY_CARRIER;
    }

    @Override
    public void validate(Shipment shipment) {
        requireMetadataField(shipment.getMetadata(), "carrierName");
        requireMetadataField(shipment.getMetadata(), "externalTrackingId");
    }

    @Override
    public BigDecimal calculateCost(Shipment shipment) {
        return shipment.getDeclaredValue()
                .multiply(new BigDecimal("0.05"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public ShipmentStatus execute(Shipment shipment) {
        return ShipmentStatus.DELIVERED;
    }
}
