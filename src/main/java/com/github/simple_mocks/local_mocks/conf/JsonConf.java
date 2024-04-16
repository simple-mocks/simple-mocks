package com.github.simple_mocks.local_mocks.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Configuration
@AllArgsConstructor
public class JsonConf {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
