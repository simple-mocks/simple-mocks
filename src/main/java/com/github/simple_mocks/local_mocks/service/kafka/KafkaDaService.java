package com.github.simple_mocks.local_mocks.service.kafka;

import com.github.simple_mocks.error_service.exception.ServiceException;
import com.github.simple_mocks.local_mocks.api.MocksErrors;
import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerDto;
import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerShortDto;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerEntity;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntity;
import com.github.simple_mocks.local_mocks.entity.kafka.KafkaProducerPropertyEntityId;
import com.github.simple_mocks.local_mocks.repository.KafkaProducerEntityRepository;
import com.github.simple_mocks.local_mocks.repository.KafkaProducerPropertyEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class KafkaDaService {
    private final KafkaProducerEntityRepository kafkaProducerEntityRepository;
    private final KafkaProducerPropertyEntityRepository kafkaProducerPropertyEntityRepository;

    public KafkaDaService(KafkaProducerEntityRepository kafkaProducerEntityRepository,
                          KafkaProducerPropertyEntityRepository kafkaProducerPropertyEntityRepository) {
        this.kafkaProducerEntityRepository = kafkaProducerEntityRepository;
        this.kafkaProducerPropertyEntityRepository = kafkaProducerPropertyEntityRepository;
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

        return buildKafkaProducerDto(code, kafkaProducerEntity);

    }

    public List<KafkaProducerShortDto> getProducers() {
        return kafkaProducerEntityRepository.findAll()
                .stream()
                .map(
                        it -> KafkaProducerShortDto.builder()
                                .code(it.getCode())
                                .description(it.getDescription())
                                .build()
                )
                .toList();
    }

    private KafkaProducerDto buildKafkaProducerDto(String it, KafkaProducerEntity it1) {
        var properties = kafkaProducerPropertyEntityRepository.findAllById_Producer(it)
                .stream()
                .collect(Collectors.toMap(prop -> prop.getId().getKey(), KafkaProducerPropertyEntity::getValue));

        return KafkaProducerDto.builder()
                .code(it1.getCode())
                .description(it1.getDescription())
                .bootstrapServers(it1.getBootstrapServers())
                .properties(properties)
                .build();
    }

    @Transactional
    public void deleteProducer(String code) {
        kafkaProducerPropertyEntityRepository.deleteAllById_Producer(code);
        kafkaProducerEntityRepository.deleteById(code);
    }
}
