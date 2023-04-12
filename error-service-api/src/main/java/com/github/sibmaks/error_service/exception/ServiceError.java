package com.github.sibmaks.error_service.exception;

/**
 * Service error type
 *
 * @author sibmaks
 * @since 2023-04-11
 */
public interface ServiceError {

    /**
     * System code what cause an error
     *
     * @return system code
     * @see com.github.sibmaks.error_service.ErrorService
     */
    String getSystemCode();

    /**
     * Error code
     *
     * @return error code
     * @see com.github.sibmaks.error_service.ErrorService
     */
    String getErrorCode();

}
