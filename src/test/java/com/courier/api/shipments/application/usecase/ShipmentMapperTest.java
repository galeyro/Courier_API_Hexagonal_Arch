package com.courier.api.shipments.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ShipmentMapperTest {

    @Test
    void toResponse_ShouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID senderId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Shipment shipment = new Shipment(
                id, senderId, recipientId, new BigDecimal("100000"), new BigDecimal("5000"),
                ShipmentType.STANDARD, ShipmentStatus.DELIVERED, Map.of("key", "value"), now, now
        );

        ShipmentResponse response = ShipmentMapper.toResponse(shipment);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(senderId, response.senderId());
        assertEquals(recipientId, response.recipientId());
        assertEquals(new BigDecimal("100000"), response.declaredValue());
        assertEquals(new BigDecimal("5000"), response.shippingCost());
        assertEquals(ShipmentType.STANDARD, response.type());
        assertEquals(ShipmentStatus.DELIVERED, response.status());
        assertEquals(Map.of("key", "value"), response.metadata());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());
    }
}
