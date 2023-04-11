package com.github.sibmaks.storage_service.local.conf;

import org.springframework.context.annotation.Bean;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalStorageServiceConfig {
    @Bean
    public LocalStorageServiceEnabled localStorageServiceEnabled() {
        return new LocalStorageServiceEnabled();
    }

}
