package com.courier.api.customers.domain.exception;

import com.courier.api.shared.domain.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
