package com.github.sibmaks.error_service;


import com.github.sibmaks.error_service.api.ErrorDescription;

/**
 * Error service - service for get error description by system code and error core
 *
 * @author sibmaks
 * @since 2023-04-11
 */
public interface ErrorService {

    /**
     * Get error description by system code and error code
     *
     * @param systemCode system code
     * @param code error code
     * @return description of error or null
     */
    ErrorDescription getDescription(String systemCode, String code);

}
