package com.github.simple_mocks.local_mocks.entity.kafka;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KAFKA_PRODUCER_PROPERTY")
public class KafkaProducerPropertyEntity {
    @EmbeddedId
    private KafkaProducerPropertyEntityId id;
    @Column(name = "value", nullable = false)
    private String value;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;
}
