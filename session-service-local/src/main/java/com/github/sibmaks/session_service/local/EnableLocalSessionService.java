package com.github.sibmaks.session_service.local;

import com.github.sibmaks.session_service.local.conf.LocalSessionServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LocalSessionServiceConfig.class)
public @interface EnableLocalSessionService {
    String value();
}
