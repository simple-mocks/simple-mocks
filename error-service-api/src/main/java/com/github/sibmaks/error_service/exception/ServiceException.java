package com.github.sibmaks.error_service.exception;

import com.github.sibmaks.error_service.ServiceError;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class ServiceException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public ServiceException(ServiceError serviceError, String systemMessage) {
        super(systemMessage);
        this.serviceError = serviceError;
    }

    public ServiceException(ServiceError serviceError, String systemMessage, Throwable cause) {
        super(systemMessage, cause);
        this.serviceError = serviceError;
    }
}
