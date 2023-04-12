package com.github.sibmaks.session_service.local.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.session_service.local.EnableLocalSessionService;
import com.github.sibmaks.session_service.local.service.LocalSessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalSessionServiceConfig implements ImportAware {
    private String configLocation;

    @Bean
    public LocalSessionServiceEnabled localSessionServiceEnabled() {
        return new LocalSessionServiceEnabled();
    }

    @Bean
    public LocalSessionService sessionService(ObjectMapper objectMapper, DefaultResourceLoader resourceLoader) throws IOException {
        var config = resourceLoader.getResource(configLocation);
        return new LocalSessionService(config, objectMapper);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        var attributes = importMetadata.getAnnotationAttributes(EnableLocalSessionService.class.getName());
        if(attributes == null) {
            throw new IllegalStateException("No attributes in annotation @EnableLocalSessionService");
        }
        var resourcePath = (String) attributes.get("value");
        if(resourcePath == null || resourcePath.isBlank()) {
            throw new IllegalArgumentException("value of @EnableLocalSessionService should not be null or blank");
        }
        this.configLocation = resourcePath;
    }
}
