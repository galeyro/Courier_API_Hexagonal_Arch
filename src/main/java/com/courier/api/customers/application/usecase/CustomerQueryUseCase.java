package com.courier.api.customers.application.usecase;

import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerQueryUseCase {

    private final CustomerRepositoryPort customerRepository;

    public CustomerQueryUseCase(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));
    }
}
