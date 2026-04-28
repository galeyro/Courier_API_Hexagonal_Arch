package com.courier.api.customers.domain.ports;

import com.courier.api.customers.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    Optional<Customer> findActiveById(UUID id);

    Optional<Customer> findByEmail(String email);

    List<Customer> findAll();
}
