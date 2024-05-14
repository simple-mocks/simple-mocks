package com.github.simple_mocks.local_mocks.repository;

import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntity;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public interface KafkaProducerPropertyEntityRepository extends
        JpaRepository<KafkaProducerPropertyEntity, KafkaProducerPropertyEntityId> {

    /**
     * Get all properties for specified KafkaProducer
     *
     * @param code kafka producer code
     * @return list of properties
     */
    List<KafkaProducerPropertyEntity> findAllById_Producer(String code);

    /**
     * Delete all properties for Kafka producer
     *
     * @param code kafka producer code
     */
    void deleteAllById_Producer(String code);
}
