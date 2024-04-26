package com.github.simple_mocks.app.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
public class AppConfig {

    @Bean
    public Base64.Decoder base64Decoder() {
        return Base64.getDecoder();
    }

    @Bean
    public Base64.Encoder base64Encoder() {
        return Base64.getEncoder();
    }
}
