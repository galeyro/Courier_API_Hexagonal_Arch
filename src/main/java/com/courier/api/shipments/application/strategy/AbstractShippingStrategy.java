package com.courier.api.shipments.application.strategy;

import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;

import java.math.BigDecimal;
import java.util.Map;

abstract class AbstractShippingStrategy {

    protected void requireMetadataField(Map<String, Object> metadata, String key) {
        Object value = metadata.get(key);
        if (value == null || value.toString().isBlank()) {
            throw new InvalidShipmentException("metadata." + key + " is required");
        }
    }

    protected BigDecimal decimalValue(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidShipmentException("metadata." + fieldName + " is required");
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException ex) {
            throw new InvalidShipmentException("metadata." + fieldName + " must be a valid number");
        }
    }

    protected void ensureDifferentParties(Shipment shipment) {
        if (shipment.getSenderId().equals(shipment.getRecipientId())) {
            throw new InvalidShipmentException("Sender and recipient must be different customers");
        }
    }
}
