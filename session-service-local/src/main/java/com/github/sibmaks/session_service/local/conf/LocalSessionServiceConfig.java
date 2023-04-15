package com.github.sibmaks.session_service.local.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.session_service.local.EnableLocalSessionService;
import com.github.sibmaks.session_service.local.service.LocalSessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.List;

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
    @Qualifier("localSessionCacheManager")
    public CacheManager localSessionCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("sessions")));
        cacheManager.initializeCaches();
        return new TransactionAwareCacheManagerProxy(cacheManager);
    }

    @Bean
    public LocalSessionService sessionService(ObjectMapper objectMapper,
                                              DefaultResourceLoader resourceLoader,
                                              @Qualifier("localSessionCacheManager") CacheManager cacheManager) throws IOException {
        var config = resourceLoader.getResource(configLocation);
        return new LocalSessionService(config, objectMapper, cacheManager);
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
