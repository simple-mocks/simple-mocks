package com.github.sibmaks.error_service.local.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error_service.ErrorService;
import com.github.sibmaks.error_service.local.EnableLocalErrorService;
import com.github.sibmaks.error_service.local.service.LocalErrorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalErrorServiceConfig implements ImportAware {
    private String configLocation;

    @Bean
    public ErrorService errorService(ObjectMapper objectMapper, DefaultResourceLoader resourceLoader) throws IOException {
        var errorsConfig = resourceLoader.getResource(configLocation);
        return new LocalErrorService(errorsConfig, objectMapper);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        var attributes = importMetadata.getAnnotationAttributes(EnableLocalErrorService.class.getName());
        if(attributes == null) {
            throw new IllegalStateException("No attributes in annotation @EnableLocalErrorService");
        }
        var resourcePath = (String) attributes.get("value");
        if(resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("value of @EnableLocalErrorService should not be null or blank");
        }
        this.configLocation = resourcePath;
    }

}
