package com.courier.api.customers.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final CustomerRole role;
    private final boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Customer(
            UUID id,
            String name,
            String email,
            String password,
            CustomerRole role,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Customer newCustomer(String name, String email, String password, CustomerRole role) {
        LocalDateTime now = LocalDateTime.now();
        return new Customer(null, name, email, password, role, true, now, now);
    }

    public Customer update(String newName, CustomerRole newRole) {
        return new Customer(id, newName, email, password, newRole, active, createdAt, LocalDateTime.now());
    }

    public Customer deactivate() {
        return new Customer(id, name, email, password, role, false, createdAt, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public CustomerRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
