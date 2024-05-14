package com.github.simple_mocks.local_mocks.api.kafka.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteKafkaProducerRq implements Serializable {
    private String code;
}