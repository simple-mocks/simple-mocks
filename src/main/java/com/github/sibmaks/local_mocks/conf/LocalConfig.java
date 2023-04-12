package com.github.sibmaks.local_mocks.conf;

import com.github.sibmaks.error_service.local.EnableLocalErrorService;
import com.github.sibmaks.session_service.local.EnableLocalSessionService;
import com.github.sibmaks.storage_service.local.EnableLocalStorageService;
import org.springframework.context.annotation.Configuration;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Configuration
@EnableLocalStorageService
@EnableLocalSessionService("classpath:/config/mocks/session.json")
@EnableLocalErrorService("classpath:/config/mocks/errors.json")
public class LocalConfig {
}
