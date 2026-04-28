package com.courier.api.shipments.application.dto;

import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ShipmentResponse(
        UUID id,
        UUID senderId,
        UUID recipientId,
        BigDecimal declaredValue,
        BigDecimal shippingCost,
        ShipmentType type,
        ShipmentStatus status,
        Map<String, Object> metadata,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
