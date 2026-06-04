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

class InternationalShippingStrategyTest {

    private final InternationalShippingStrategy strategy = new InternationalShippingStrategy();

    @Test
    void supportedType_ShouldReturnInternational() {
        assertEquals(ShipmentType.INTERNATIONAL, strategy.supportedType());
    }

    @Test
    void validate_ShouldSucceed_WhenAllFieldsAreValid() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("destinationCountry", "Spain");
        metadata.put("customsDeclaration", "Gifts");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000000"), ShipmentType.INTERNATIONAL, metadata);

        assertDoesNotThrow(() -> strategy.validate(shipment));
    }

    @Test
    void validate_ShouldThrowException_WhenDestinationCountryIsMissing() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("customsDeclaration", "Gifts");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000000"), ShipmentType.INTERNATIONAL, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("metadata.destinationCountry is required", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenCustomsDeclarationIsMissing() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("destinationCountry", "Spain");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000000"), ShipmentType.INTERNATIONAL, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("metadata.customsDeclaration is required", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenDeclaredValueExceedsLimit() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("destinationCountry", "Spain");
        metadata.put("customsDeclaration", "Gifts");
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("50000001"), ShipmentType.INTERNATIONAL, metadata);

        InvalidShipmentException exception = assertThrows(InvalidShipmentException.class, () -> strategy.validate(shipment));
        assertEquals("INTERNATIONAL shipment declaredValue must be <= 50000000", exception.getMessage());
    }

    @Test
    void calculateCost_ShouldIncludeBaseAndVariableCost() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000000"), ShipmentType.INTERNATIONAL, Map.of());
        // base cost is 50000. variable cost is 1000000 * 0.02 = 20000.00. Total = 70000.00.
        assertEquals(new BigDecimal("70000.00"), strategy.calculateCost(shipment));
    }

    @Test
    void execute_ShouldReturnInCustoms() {
        Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100000"), ShipmentType.INTERNATIONAL, Map.of());
        assertEquals(ShipmentStatus.IN_CUSTOMS, strategy.execute(shipment));
    }
}
