package com.courier.api.shipments.application.dto;

import com.courier.api.shipments.domain.model.ShipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Schema(description = "Payload to create a shipment")
public record CreateShipmentRequest(
        @NotNull UUID senderId,
        @NotNull UUID recipientId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal declaredValue,
        @NotNull ShipmentType type,
        @NotNull Map<String, Object> metadata
) {
}
