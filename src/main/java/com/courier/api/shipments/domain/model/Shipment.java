package com.courier.api.shipments.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Shipment {

    private final UUID id;
    private final UUID senderId;
    private final UUID recipientId;
    private final BigDecimal declaredValue;
    private final BigDecimal shippingCost;
    private final ShipmentType type;
    private final ShipmentStatus status;
    private final Map<String, Object> metadata;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Shipment(
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
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.declaredValue = declaredValue;
        this.shippingCost = shippingCost;
        this.type = type;
        this.status = status;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Shipment pending(
            UUID senderId,
            UUID recipientId,
            BigDecimal declaredValue,
            ShipmentType type,
            Map<String, Object> metadata
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new Shipment(null, senderId, recipientId, declaredValue, BigDecimal.ZERO, type, ShipmentStatus.PENDING, metadata, now, now);
    }

    public Shipment processed(BigDecimal newShippingCost, ShipmentStatus newStatus) {
        return new Shipment(
                id,
                senderId,
                recipientId,
                declaredValue,
                newShippingCost,
                type,
                newStatus,
                metadata,
                createdAt,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public BigDecimal getDeclaredValue() {
        return declaredValue;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public ShipmentType getType() {
        return type;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
