package com.courier.api.customers.infrastructure.in.rest;

import com.courier.api.customers.application.dto.CreateCustomerRequest;
import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.application.dto.UpdateCustomerRequest;
import com.courier.api.customers.application.usecase.CustomerCommandUseCase;
import com.courier.api.customers.application.usecase.CustomerQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for customer management.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerCommandUseCase customerCommandUseCase;
    private final CustomerQueryUseCase customerQueryUseCase;

    /**
     * Constructs a new CustomerController.
     *
     * @param customerCommandUseCase the command use case
     * @param customerQueryUseCase   the query use case
     */
    public CustomerController(
            CustomerCommandUseCase customerCommandUseCase,
            CustomerQueryUseCase customerQueryUseCase
    ) {
        this.customerCommandUseCase = customerCommandUseCase;
        this.customerQueryUseCase = customerQueryUseCase;
    }

    /**
     * Creates a new customer.
     *
     * @param request the customer creation request
     * @return the created customer
     */
    @Operation(summary = "Create a customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest request) {
        return customerCommandUseCase.create(request);
    }

    /**
     * Returns all customers.
     *
     * @return list of customers
     */
    @Operation(summary = "List customers")
    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerQueryUseCase.findAll();
    }

    /**
     * Finds a customer by ID.
     *
     * @param id the customer ID
     * @return the customer
     */
    @Operation(summary = "Find customer by id")
    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable UUID id) {
        return customerQueryUseCase.findById(id);
    }

    /**
     * Updates a customer.
     *
     * @param id      the customer ID
     * @param request the update request
     * @return the updated customer
     */
    @Operation(summary = "Update a customer")
    @PatchMapping("/{id}")
    public CustomerResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest request) {
        return customerCommandUseCase.update(id, request);
    }

    /**
     * Deactivates a customer.
     *
     * @param id the customer ID
     */
    @Operation(summary = "Soft delete a customer")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        customerCommandUseCase.deactivate(id);
    }
}
