package com.courier.api.shipments.infrastructure.in.rest;

import com.courier.api.shipments.application.dto.CreateShipmentRequest;
import com.courier.api.shipments.application.dto.ShipmentResponse;
import com.courier.api.shipments.application.usecase.CreateShipmentUseCase;
import com.courier.api.shipments.application.usecase.ShipmentQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for shipment management.
 */
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final ShipmentQueryUseCase shipmentQueryUseCase;

    /**
     * Constructs a new ShipmentController.
     */
    public ShipmentController(CreateShipmentUseCase createShipmentUseCase, ShipmentQueryUseCase shipmentQueryUseCase) {
        this.createShipmentUseCase = createShipmentUseCase;
        this.shipmentQueryUseCase = shipmentQueryUseCase;
    }

    /**
     * Creates a new shipment.
     */
    @Operation(summary = "Create a shipment")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse create(@Valid @RequestBody CreateShipmentRequest request) {
        return createShipmentUseCase.create(request);
    }

    /**
     * Finds a shipment by ID.
     */
    @Operation(summary = "Find shipment by id")
    @GetMapping("/{id}")
    public ShipmentResponse findById(@PathVariable UUID id) {
        return shipmentQueryUseCase.findById(id);
    }

    /**
     * Finds shipments by customer ID.
     */
    @Operation(summary = "Find shipments sent or received by customer")
    @GetMapping("/customer/{id}")
    public List<ShipmentResponse> findByCustomerId(@PathVariable UUID id) {
        return shipmentQueryUseCase.findByCustomerId(id);
    }
}
