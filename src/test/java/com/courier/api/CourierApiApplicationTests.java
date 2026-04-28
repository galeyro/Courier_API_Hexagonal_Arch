package com.courier.api;

import com.courier.api.shipments.application.strategy.ExpressShippingStrategy;
import com.courier.api.shipments.application.strategy.InternationalShippingStrategy;
import com.courier.api.shipments.application.strategy.StandardShippingStrategy;
import com.courier.api.shipments.application.strategy.ThirdPartyCarrierShippingStrategy;
import com.courier.api.shipments.domain.model.Shipment;
import com.courier.api.shipments.domain.model.ShipmentStatus;
import com.courier.api.shipments.domain.model.ShipmentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourierApiApplicationTests {

	@Test
	void shouldCalculateStandardShipping() {
		Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("1000000"), ShipmentType.STANDARD, Map.of("weightKg", 10));
		StandardShippingStrategy strategy = new StandardShippingStrategy();

		assertEquals(new BigDecimal("5000"), strategy.calculateCost(shipment));
		assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
	}

	@Test
	void shouldCalculateExpressShipping() {
		Shipment shipment = Shipment.pending(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("2500000"), ShipmentType.EXPRESS, Map.of("weightKg", 4));
		ExpressShippingStrategy strategy = new ExpressShippingStrategy();

		assertEquals(new BigDecimal("15000"), strategy.calculateCost(shipment));
		assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
	}

	@Test
	void shouldCalculateInternationalShipping() {
		Shipment shipment = Shipment.pending(
				UUID.randomUUID(),
				UUID.randomUUID(),
				new BigDecimal("1000000"),
				ShipmentType.INTERNATIONAL,
				Map.of("destinationCountry", "EC", "customsDeclaration", "Books")
		);
		InternationalShippingStrategy strategy = new InternationalShippingStrategy();

		assertEquals(new BigDecimal("70000.00"), strategy.calculateCost(shipment));
		assertEquals(ShipmentStatus.IN_CUSTOMS, strategy.execute(shipment));
	}

	@Test
	void shouldCalculateThirdPartyShipping() {
		Shipment shipment = Shipment.pending(
				UUID.randomUUID(),
				UUID.randomUUID(),
				new BigDecimal("800000"),
				ShipmentType.THIRD_PARTY_CARRIER,
				Map.of("carrierName", "DHL", "externalTrackingId", "TRK-001")
		);
		ThirdPartyCarrierShippingStrategy strategy = new ThirdPartyCarrierShippingStrategy();

		assertEquals(new BigDecimal("40000.00"), strategy.calculateCost(shipment));
		assertEquals(ShipmentStatus.DELIVERED, strategy.execute(shipment));
	}
}
