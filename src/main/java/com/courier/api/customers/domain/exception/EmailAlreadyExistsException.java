package com.courier.api.customers.domain.exception;

import com.courier.api.shared.domain.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
