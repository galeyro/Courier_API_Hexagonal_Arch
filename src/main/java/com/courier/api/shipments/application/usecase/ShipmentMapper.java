package com.courier.api.shipments.application.usecase;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.model.Shipment;

public final class ShipmentMapper {

    private ShipmentMapper() {
    }

    public static ShipmentResponse toResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getSenderId(),
                shipment.getRecipientId(),
                shipment.getDeclaredValue(),
                shipment.getShippingCost(),
                shipment.getType(),
                shipment.getStatus(),
                shipment.getMetadata(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt()
        );
    }
}
