package com.github.sibmaks.error_service.exception;

import lombok.Getter;

/**
 * Common service exception.<br/>
 * Should be thrown when something wrong happened in logically usage of APIs
 *
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
