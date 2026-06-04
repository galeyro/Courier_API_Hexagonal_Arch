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

class ExpressShippingStrategyTest {

    private final ExpressShippingStrategy strategy = new ExpressShippingStrategy();

    @Test
    void supportedType_ShouldReturnExpress() {
        assertEquals(ShipmentType.EXPRESS, strategy.supportedType());
    }

    @Test
    void validate_ShouldSucceed_WhenWeightAndDeclaredValueAreValid() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "3.5");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, metadata);

        assertDoesNotThrow(() -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenWeightIsMissing() {
        Map<String, Object> metadata = new HashMap<>();
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, metadata);

        assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenWeightIsNotANumber() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "invalid");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, metadata);

        assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenWeightExceedsLimit() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "5.1");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("EXPRESS shipment weight must be <= 5 kg", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenDeclaredValueExceedsLimit() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "4.0");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("3000001"), ShipmentType.EXPRESS, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("EXPRESS shipment declaredValue must be <= 3000000", exception.getMessage());
    }

    @Test
    void calculateCost_ShouldReturnFixedCost() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, Map.of());
        assertEquals(new BigDecimal("15000"), strategy.calculateCost(shipment));
    }

    @Test
    void execute_ShouldReturnDelivered() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.EXPRESS, Map.of());
        assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
    }
}
