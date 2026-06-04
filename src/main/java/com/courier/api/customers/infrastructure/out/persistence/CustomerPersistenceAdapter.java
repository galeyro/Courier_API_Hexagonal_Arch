package com.courier.api.customers.infrastructure.out.persistence;

import com.courier.api.customers.domain.model.Customer;
import com.courier.api.customers.domain.ports.CustomerRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing customer repository port using JPA.
 */
@Component
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository customerJpaRepository;

    /**
     * Constructs a new CustomerPersistenceAdapter.
     *
     * @param customerJpaRepository the JPA repository for customer entities
     */
    public CustomerPersistenceAdapter(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return CustomerEntityMapper.toDomain(customerJpaRepository.save(CustomerEntityMapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerJpaRepository.findById(id).map(CustomerEntityMapper::toDomain);
    }

    @Override
    public Optional<Customer> findActiveById(UUID id) {
        return customerJpaRepository.findByIdAndActiveTrue(id).map(CustomerEntityMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerJpaRepository.findByEmail(email).map(CustomerEntityMapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream().map(CustomerEntityMapper::toDomain).toList();
    }
}
