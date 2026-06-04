package com.courier.api.shipments.domain.exception;

import com.courier.api.shared.domain.exception.ValidationException;

public class InvalidShipmentException extends ValidationException {

    public InvalidShipmentException(String message) {
        super(message);
    }

    public InvalidShipmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
