package com.courier.api.customers.application.usecase;

import com.courier.api.customers.application.dto.CreateCustomerRequest;
import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.application.dto.UpdateCustomerRequest;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.exception.EmailAlreadyExistsException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import com.courier.api.customers.domain.ports.PasswordHasherPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for customer write operations.
 */
@Service
public class CustomerCommandUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final PasswordHasherPort passwordHasher;

    /**
     * Constructs a new CustomerCommandUseCase.
     *
     * @param customerRepository the customer repository port
     * @param passwordHasher     the password hasher port
     */
    public CustomerCommandUseCase(CustomerRepositoryPort customerRepository, PasswordHasherPort passwordHasher) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Creates a new customer.
     *
     * @param request the customer creation request
     * @return the created customer response
     * @throws EmailAlreadyExistsException if email is already in use
     */
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        customerRepository.findByEmail(request.email())
                .ifPresent(customer -> {
                    throw new EmailAlreadyExistsException("Email already exists: " + request.email());
                });

        Customer customer = Customer.newCustomer(
                request.name().trim(),
                request.email().trim().toLowerCase(),
                passwordHasher.hash(request.password()),
                request.role()
        );

        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    /**
     * Updates an existing customer.
     *
     * @param id      the customer ID
     * @param request the update request
     * @return the updated customer response
     * @throws CustomerNotFoundException if customer is not found
     */
    @Transactional
    public CustomerResponse update(UUID id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));

        Customer updated = customer.update(request.name().trim(), request.role());
        return CustomerMapper.toResponse(customerRepository.save(updated));
    }

    /**
     * Deactivates a customer by ID.
     *
     * @param id the customer ID
     * @throws CustomerNotFoundException if customer is not found
     */
    @Transactional
    public void deactivate(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));

        customerRepository.save(customer.deactivate());
    }
}
