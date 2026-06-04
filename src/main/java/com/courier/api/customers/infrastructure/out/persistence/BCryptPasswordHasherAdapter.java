package com.courier.api.customers.infrastructure.out.persistence;

import com.courier.api.customers.domain.ports.PasswordHasherPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adapter for BCrypt password hashing.
 */
@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new BCryptPasswordHasherAdapter.
     *
     * @param passwordEncoder the password encoder
     */
    public BCryptPasswordHasherAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param rawPassword the plain text password to hash
     * @return the hashed password
     */
    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
