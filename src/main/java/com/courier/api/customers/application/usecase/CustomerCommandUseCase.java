package com.courier.api.customers.application.usecase;

import com.courier.api.customers.application.dto.CreateCustomerRequest;
import com.courier.api.customers.application.dto.CustomerResponse;
import com.courier.api.customers.application.dto.UpdateCustomerRequest;
import com.courier.api.customers.domain.exception.CustomerNotFoundException;
import com.courier.api.customers.domain.exception.EmailAlreadyExistsException;
import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import com.courier.api.customers.domain.ports.PasswordHasherPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerCommandUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final PasswordHasherPort passwordHasher;

    public CustomerCommandUseCase(CustomerRepositoryPort customerRepository, PasswordHasherPort passwordHasher) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

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

    @Transactional
    public CustomerResponse update(UUID id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));

        return CustomerMapper.toResponse(customerRepository.save(customer.update(request.name().trim(), request.role())));
    }

    @Transactional
    public void deactivate(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + id));

        customerRepository.save(customer.deactivate());
    }
}
