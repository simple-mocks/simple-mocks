package com.github.sibmaks.error_service.local;

import com.github.sibmaks.error_service.local.conf.LocalErrorServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LocalErrorServiceConfig.class)
public @interface EnableLocalErrorService {
    String value();
}
