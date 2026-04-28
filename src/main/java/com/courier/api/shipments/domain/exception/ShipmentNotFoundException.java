package com.courier.api.shipments.domain.exception;

import com.courier.api.shared.domain.exception.NotFoundException;

public class ShipmentNotFoundException extends NotFoundException {

    public ShipmentNotFoundException(String message) {
        super(message);
    }
}
