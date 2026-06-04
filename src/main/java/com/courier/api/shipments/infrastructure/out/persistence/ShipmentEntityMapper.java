package com.courier.api.shipments.infrastructure.out.persistence;

import com.courier.api.shipments.domain.model.Shipment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;

/**
 * Maps between shipment domain objects and JPA entities.
 */
public class ShipmentEntityMapper {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new ShipmentEntityMapper.
     */
    public ShipmentEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Converts a domain object to a JPA entity.
     */
    public ShipmentEntity toEntity(Shipment shipment) {
        ShipmentEntity entity = new ShipmentEntity();
        entity.setId(shipment.getId());
        entity.setSenderId(shipment.getSenderId());
        entity.setRecipientId(shipment.getRecipientId());
        entity.setDeclaredValue(shipment.getDeclaredValue());
        entity.setShippingCost(shipment.getShippingCost());
        entity.setType(shipment.getType());
        entity.setStatus(shipment.getStatus());
        entity.setMetadata(writeMetadata(shipment.getMetadata()));
        entity.setCreatedAt(shipment.getCreatedAt());
        entity.setUpdatedAt(shipment.getUpdatedAt());
        return entity;
    }

    /**
     * Converts a JPA entity to a domain object.
     */
    public Shipment toDomain(ShipmentEntity entity) {
        return new Shipment(
                entity.getId(),
                entity.getSenderId(),
                entity.getRecipientId(),
                entity.getDeclaredValue(),
                entity.getShippingCost(),
                entity.getType(),
                entity.getStatus(),
                readMetadata(entity.getMetadata()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private String writeMetadata(Map<String, Object> metadata) {
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize shipment metadata", ex);
        }
    }

    private Map<String, Object> readMetadata(String metadata) {
        try {
            return objectMapper.readValue(metadata, new TypeReference<>() {
            });
        } catch (JsonProcessingException ex) {
            return Collections.emptyMap();
        }
    }
}
