package com.github.simple_mocks.local_mocks.api.kafka.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendKafkaProducerRq implements Serializable {
    private String code;
    private Map<String, String> additionalProperties;
    private String topic;
    private Integer partition;
    private Long timestamp;
    private byte[] key;
    private byte[] value;
    private Map<String, byte[]> headers;
}
