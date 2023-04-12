package com.github.sibmaks.storage_service.api;

import com.github.sibmaks.error_service.exception.ServiceError;
import lombok.AllArgsConstructor;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@AllArgsConstructor
public enum StorageErrors implements ServiceError {
    NOT_FOUND;


    @Override
    public String getErrorCode() {
        return name();
    }

    @Override
    public String getSystemCode() {
        return "STORAGE";
    }
}
