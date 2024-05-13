package com.github.simple_mocks.local_mocks.entity.kafka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.ZonedDateTime;

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
@Table(name = "KAFKA_PRODUCER")
public class KafkaProducerEntity {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "description")
    private String description;
    @Column(name = "bootstrap_servers", nullable = false)
    private String bootstrapServers;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;
}
