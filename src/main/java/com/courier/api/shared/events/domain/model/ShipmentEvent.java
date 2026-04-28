package com.courier.api.shared.events.domain.model;

import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentEvent(
        UUID shipmentId,
        UUID senderId,
        UUID recipientId,
        BigDecimal declaredValue,
        BigDecimal shippingCost,
        ShipmentType type,
        ShipmentStatus status,
        LocalDateTime timestamp
) {
}
