package com.github.simple_mocks.local_mocks.entity.kafka;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KafkaProducerPropertyEntityId implements Serializable {
    @Column(name = "kafka_producer_code", nullable = false)
    private String producer;
    @Column(name = "key", nullable = false, length = 512)
    private String key;
}
