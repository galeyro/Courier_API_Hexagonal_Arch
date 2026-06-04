package com.courier.api.customers.domain.ports;

/**
 * Port for password hashing operations.
 */
public interface PasswordHasherPort {

    /**
     * Hashes a plain text password.
     *
     * @param plainPassword the plain text password
     * @return the hashed password
     */
    String hash(String plainPassword);
}
