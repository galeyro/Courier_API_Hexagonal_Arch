package com.courier.api.shipments.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Shipment {

    //model
    private UUID id;
    private UUID senderId;
    private UUID recipientId;
    private BigDecimal declaredValue;
    private BigDecimal shippingCost;
    private ShipmentType type;
    private ShipmentStatus status;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
