package com.courier.api.shipments.application.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.courier.api.shipments.domain.exception.InvalidShipmentException;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ThirdPartyCarrierShippingStrategyTest {

    private final ThirdPartyCarrierShippingStrategy strategy = new ThirdPartyCarrierShippingStrategy();

    @Test
    void supportedType_ShouldReturnThirdPartyCarrier() {
        assertEquals(ShipmentType.THIRD_PARTY_CARRIER, strategy.supportedType());
    }

    @Test
    void validate_ShouldSucceed_WhenAllFieldsAreValid() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("carrierName", "DHL");
        metadata.put("externalTrackingId", "12345");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.THIRD_PARTY_CARRIER, metadata);

        assertDoesNotThrow(() -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenCarrierNameIsMissing() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("externalTrackingId", "12345");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.THIRD_PARTY_CARRIER, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("metadata.carrierName is required", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenExternalTrackingIdIsMissing() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("carrierName", "DHL");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.THIRD_PARTY_CARRIER, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("metadata.externalTrackingId is required", exception.getMessage());
    }

    @Test
    void calculateCost_ShouldReturnFivePercentOfDeclaredValue() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.THIRD_PARTY_CARRIER, Map.of());
        assertEquals(new BigDecimal("5000.00"), strategy.calculateCost(shipment));
    }

    @Test
    void execute_ShouldReturnDelivered() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.THIRD_PARTY_CARRIER, Map.of());
        assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
    }
}
