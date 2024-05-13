package com.github.simple_mocks.local_mocks.service.kafka;

import com.github.simple_mocks.error_service.exception.ServiceException;
import com.github.simple_mocks.local_mocks.api.MocksErrors;
import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerDto;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerEntity;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntity;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntityId;
import com.github.simple_mocks.local_mocks.repository.KafkaProducerEntityRepository;
import com.github.simple_mocks.local_mocks.repository.KafkaProducerPropertyEntityRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class KafkaFacadeService {
    private final KafkaProducerEntityRepository kafkaProducerEntityRepository;
    private final KafkaProducerPropertyEntityRepository kafkaProducerPropertyEntityRepository;

    public KafkaFacadeService(KafkaProducerEntityRepository kafkaProducerEntityRepository,
                              KafkaProducerPropertyEntityRepository kafkaProducerPropertyEntityRepository) {
        this.kafkaProducerEntityRepository = kafkaProducerEntityRepository;
        this.kafkaProducerPropertyEntityRepository = kafkaProducerPropertyEntityRepository;
    }

    /**
     * Send event into Kafka producer saved with code {@code code} into a specified topic.
     *
     * @param code    kafka producer code
     * @param topic   kafka topic
     * @param key     event key
     * @param value   event value
     * @param headers event headers
     * @return true if successful
     */
    public boolean produce(String code,
                           String topic,
                           byte[] key,
                           byte[] value,
                           Map<String, byte[]> headers) {
        return produce(
                code,
                Collections.emptyMap(),
                topic,
                null,
                null,
                key,
                value,
                headers
        );
    }

    /**
     * Send event into Kafka producer saved with code {@code code} into a specified topic and partition.
     * Configure additional properties at the end of settings.
     *
     * @param code                 kafka producer code
     * @param additionalProperties kafka producer additional properties
     * @param topic                kafka topic
     * @param partition            topic partition
     * @param timestamp            event timestamp
     * @param key                  event key
     * @param value                event value
     * @param headers              event headers
     * @return true if successful
     */
    public boolean produce(String code,
                           Map<String, String> additionalProperties,
                           String topic,
                           Integer partition,
                           Long timestamp,
                           byte[] key,
                           byte[] value,
                           Map<String, byte[]> headers) {
        var kafkaProducerEntity = kafkaProducerEntityRepository.findById(code)
                .orElseThrow(
                        () -> new ServiceException(
                                MocksErrors.UNEXPECTED_ERROR,
                                "Kafka producer %s not found".formatted(code)
                        )
                );
        var properties = getProperties(kafkaProducerEntity, additionalProperties);

        var recordHeaders = new RecordHeaders();
        if (headers != null) {
            for (var headerEntry : headers.entrySet()) {
                recordHeaders.add(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        Future<RecordMetadata> metadataFuture;
        try (var producer = new KafkaProducer<byte[], byte[]>(properties)) {

            var producerRecord = new ProducerRecord<>(topic, partition, timestamp, key, value, recordHeaders);

            metadataFuture = producer.send(producerRecord);
        }
        try {
            metadataFuture.get();
            return true;
        } catch (ExecutionException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private Properties getProperties(KafkaProducerEntity kafkaProducerEntity,
                                     Map<String, String> additionalProperties) {
        var code = kafkaProducerEntity.getCode();
        var bootstrapServers = kafkaProducerEntity.getBootstrapServers();

        var properties = new Properties();

        var staticProperties = kafkaProducerPropertyEntityRepository.findAllById_Producer(code);

        for (var staticProperty : staticProperties) {
            var id = staticProperty.getId();
            properties.put(id.getKey(), staticProperty.getValue());
        }

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());


        if (additionalProperties != null) {
            properties.putAll(additionalProperties);
        }
        return properties;
    }

    @Transactional
    public void createProducer(String code,
                               String description,
                               String bootstrapServers,
                               Map<String, String> properties) {

        var kafkaProducerEntity = KafkaProducerEntity.builder()
                .code(code.toLowerCase())
                .description(description)
                .bootstrapServers(bootstrapServers)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .build();

        var savedKafkaProducerEntity = kafkaProducerEntityRepository.save(kafkaProducerEntity);

        if (properties == null || properties.isEmpty()) {
            return;
        }

        var kafkaProducerPropertyEntities = properties.entrySet()
                .stream()
                .map(it -> KafkaProducerPropertyEntity.builder()
                        .id(KafkaProducerPropertyEntityId.builder()
                                .producer(savedKafkaProducerEntity.getCode())
                                .key(it.getKey())
                                .build()
                        )
                        .value(it.getValue())
                        .createdAt(ZonedDateTime.now())
                        .modifiedAt(ZonedDateTime.now())
                        .build())
                .toList();

        kafkaProducerPropertyEntityRepository.saveAll(kafkaProducerPropertyEntities);
    }

    @Transactional
    public void updateProducer(String code,
                               String description,
                               String bootstrapServers,
                               Map<String, String> properties) {
        var kafkaProducerEntity = kafkaProducerEntityRepository.findById(code)
                .orElseThrow(
                        () -> new ServiceException(
                                MocksErrors.UNEXPECTED_ERROR,
                                "Kafka producer %s not found".formatted(code)
                        )
                );
        kafkaProducerEntity.setDescription(description);
        kafkaProducerEntity.setBootstrapServers(bootstrapServers);
        kafkaProducerEntity.setModifiedAt(ZonedDateTime.now());
        kafkaProducerEntityRepository.save(kafkaProducerEntity);

        var savedProperties = kafkaProducerPropertyEntityRepository.findAllById_Producer(code)
                .stream()
                .collect(Collectors.toMap(it -> it.getId().getKey(), Function.identity()));
        var toDelete = savedProperties.values().stream()
                .map(KafkaProducerPropertyEntity::getId)
                .filter(it -> !properties.containsKey(it.getKey()))
                .toList();
        kafkaProducerPropertyEntityRepository.deleteAllById(toDelete);

        var newProperties = new ArrayList<KafkaProducerPropertyEntity>(properties.size());
        for (var propertyEntry : properties.entrySet()) {
            var savedProperty = savedProperties.get(propertyEntry.getKey());
            if (savedProperty == null) {
                savedProperty = KafkaProducerPropertyEntity.builder()
                        .id(KafkaProducerPropertyEntityId.builder()
                                .producer(code)
                                .key(propertyEntry.getKey())
                                .build()
                        )
                        .value(propertyEntry.getValue())
                        .createdAt(ZonedDateTime.now())
                        .modifiedAt(ZonedDateTime.now())
                        .build();
            } else {
                if (!Objects.equals(propertyEntry.getValue(), savedProperty.getValue())) {
                    savedProperty.setValue(propertyEntry.getValue());
                    savedProperty.setModifiedAt(ZonedDateTime.now());
                }
            }
            newProperties.add(savedProperty);
        }
        kafkaProducerPropertyEntityRepository.saveAll(newProperties);
    }

    public KafkaProducerDto getProducer(String code) {
        var kafkaProducerEntity = kafkaProducerEntityRepository.findById(code)
                .orElseThrow(
                        () -> new ServiceException(
                                MocksErrors.UNEXPECTED_ERROR,
                                "Kafka producer %s not found".formatted(code)
                        )
                );

        var properties = kafkaProducerPropertyEntityRepository.findAllById_Producer(code)
                .stream()
                .collect(Collectors.toMap(it -> it.getId().getKey(), KafkaProducerPropertyEntity::getValue));

        return KafkaProducerDto.builder()
                .code(kafkaProducerEntity.getCode())
                .description(kafkaProducerEntity.getDescription())
                .bootstrapServers(kafkaProducerEntity.getBootstrapServers())
                .properties(properties)
                .build();

    }
}
