package com.github.sibmaks.error_service.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error_service.ErrorService;
import com.github.sibmaks.error_service.api.ErrorDescription;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalErrorService implements ErrorService {
    private final Map<String, Map<String, ErrorDescription>> errorDescriptions;

    public LocalErrorService(Resource resource, ObjectMapper objectMapper) throws IOException {
        var contentArray = resource.getContentAsByteArray();
        var errorDescriptions = objectMapper.readValue(contentArray, ErrorDescription[].class);
        this.errorDescriptions = new HashMap<>();
        for (var errorDescription : errorDescriptions) {
            var systemCode = errorDescription.getSystemCode();
            var subsystemErrors = this.errorDescriptions.computeIfAbsent(systemCode, it -> new HashMap<>());
            var code = errorDescription.getCode();
            subsystemErrors.put(code, errorDescription);
        }
    }

    @Override
    public ErrorDescription getDescription(String systemCode, String code) {
        var subsystemErrors = errorDescriptions.get(systemCode);
        if(subsystemErrors == null) {
            throw new IllegalArgumentException("Error descriptions for system %s not found.".formatted(systemCode));
        }
        return subsystemErrors.get(code);
    }
}
