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

class StandardShippingStrategyTest {

    private final StandardShippingStrategy strategy = new StandardShippingStrategy();

    @Test
    void supportedType_ShouldReturnStandard() {
        assertEquals(ShipmentType.STANDARD, strategy.supportedType());
    }

    @Test
    void validate_ShouldSucceed_WhenWeightIsValidAndPartiesAreDifferent() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "15.0");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.STANDARD, metadata);

        assertDoesNotThrow(() -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenSenderAndRecipientAreSame() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "15.0");
        UUID customerId = UUID.randomUUID();
        Shipment shipment = Shipment.pending(customerId, customerId, new BigDecimal("100000"), ShipmentType.STANDARD, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("Sender and recipient must be different customers", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenWeightExceedsLimit() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("weightKg", "20.1");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.STANDARD, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("STANDARD shipment weight must be <= 20 kg", exception.getMessage());
    }

    @Test
    void calculateCost_ShouldReturnMinimumCost_WhenPercentageCostIsLower() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000"), ShipmentType.STANDARD, Map.of());
        // percentage is 1000 * 0.001 = 1.00. Minimum cost is 5000.
        assertEquals(new BigDecimal("5000"), strategy.calculateCost(shipment));
    }

    @Test
    void calculateCost_ShouldReturnPercentageCost_WhenHigherThanMinimum() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("6000000"), ShipmentType.STANDARD, Map.of());
        // percentage is 6000000 * 0.001 = 6000.00. Minimum cost is 5000.
        assertEquals(new BigDecimal("6000.00"), strategy.calculateCost(shipment));
    }

    @Test
    void execute_ShouldReturnDelivered() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.STANDARD, Map.of());
        assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
    }
}
