package com.github.sibmaks.storage_service.local;

import com.github.sibmaks.storage_service.local.conf.LocalStorageServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LocalStorageServiceConfig.class)
public @interface EnableLocalStorageService {
}
