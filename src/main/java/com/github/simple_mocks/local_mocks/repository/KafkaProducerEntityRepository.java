package com.github.simple_mocks.local_mocks.repository;

import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public interface KafkaProducerEntityRepository extends JpaRepository<KafkaProducerEntity, String> {

}
