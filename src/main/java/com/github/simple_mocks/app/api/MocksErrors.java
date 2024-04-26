package com.github.simple_mocks.app.api;

import com.github.simple_mocks.error_service.exception.ServiceError;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@AllArgsConstructor
public enum MocksErrors implements ServiceError {
    /**
     * Unexpected service error
     */
    UNEXPECTED_ERROR;


    @Nonnull
    @Override
    public String getErrorCode() {
        return name();
    }

    @Nonnull
    @Override
    public String getSystemCode() {
        return "SIMPLE_MOCKS";
    }
}
