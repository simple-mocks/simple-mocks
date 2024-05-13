package com.github.simple_mocks.local_mocks.api.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@Getter
@Builder
@AllArgsConstructor
public class KafkaProducerDto implements Serializable {
    private String code;
    private String description;
    private String bootstrapServers;
    private Map<String, String> properties;
}
