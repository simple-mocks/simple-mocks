package com.github.sibmaks.error_service;


import com.github.sibmaks.error_service.api.ErrorDescription;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface ErrorService {

    ErrorDescription getDescription(String systemCode, String code);

}
