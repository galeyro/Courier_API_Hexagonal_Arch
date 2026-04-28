package com.courier.api.customers.domain.ports;

public interface PasswordHasherPort {

    String hash(String rawPassword);
}
