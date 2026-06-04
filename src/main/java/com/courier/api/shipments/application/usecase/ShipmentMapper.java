package com.courier.api.shipments.application.usecase;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.model.Shipment;

/**
 * Maps shipment domain objects to response DTOs.
 */
public final class ShipmentMapper {

    private ShipmentMapper() {
    }

    /**
     * Converts a shipment domain object to a response DTO.
     */
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
