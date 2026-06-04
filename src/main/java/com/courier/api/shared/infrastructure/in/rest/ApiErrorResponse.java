package com.courier.api.shared.infrastructure.in.rest;

import java.time.LocalDateTime;

/**
 * API error response structure.
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
