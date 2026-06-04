package com.courier.api.customers.application.usecase;

import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for customer read operations.
 */
@Service
public class CustomerQueryUseCase {

    private final CustomerRepositoryPort customerRepository;

    /**
     * Constructs a new CustomerQueryUseCase.
     *
     * @param customerRepository the customer repository port
     */
    public CustomerQueryUseCase(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Returns all customers.
     *
     * @return list of customer responses
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    /**
     * Finds a customer by ID.
     *
     * @param id the customer ID
     * @return the customer response
     * @throws CustomerNotFoundException if customer is not found
     */
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));
    }
}
