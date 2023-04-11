package com.github.sibmaks.local_mocks.conf;

import com.github.sibmaks.error_service.local.EnableLocalErrorService;
import com.github.sibmaks.storage_service.local.EnableLocalStorageService;
import org.springframework.context.annotation.Configuration;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Configuration
@EnableLocalStorageService
@EnableLocalErrorService("classpath:/config/mocks/errors.json")
public class LocalConfig {
}
