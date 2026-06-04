package com.courier.api.customers.infrastructure.out.persistence;

import com.courier.api.customers.domain.model.Customer;

/**
 * Maps between customer domain objects and JPA entities.
 */
public final class CustomerEntityMapper {

    private CustomerEntityMapper() {
    }

    /**
     * Converts a domain object to a JPA entity.
     *
     * @param customer the domain customer object
     * @return the corresponding JPA entity
     */
    public static CustomerEntity toEntity(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setName(customer.getName());
        entity.setEmail(customer.getEmail());
        entity.setPassword(customer.getPassword());
        entity.setRole(customer.getRole());
        entity.setActive(customer.isActive());
        entity.setCreatedAt(customer.getCreatedAt());
        entity.setUpdatedAt(customer.getUpdatedAt());
        return entity;
    }

    /**
     * Converts a JPA entity to a domain object.
     *
     * @param entity the JPA customer entity
     * @return the corresponding domain customer object
     */
    public static Customer toDomain(CustomerEntity entity) {
        return new Customer(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
