package com.github.simple_mocks.local_mocks.service.kafka;

import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class KafkaProducerService {
    private final KafkaProducerDaService kafkaProducerDaService;

    public KafkaProducerService(KafkaProducerDaService kafkaProducerDaService) {
        this.kafkaProducerDaService = kafkaProducerDaService;
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
        var kafkaProducerDto = kafkaProducerDaService.getProducer(code);
        var properties = getProperties(kafkaProducerDto, additionalProperties);

        var recordHeaders = new RecordHeaders();
        if (headers != null) {
            for (var headerEntry : headers.entrySet()) {
                recordHeaders.add(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        var producerRecord = new ProducerRecord<>(topic, partition, timestamp, key, value, recordHeaders);
        var metadataFuture = produce(properties, producerRecord);

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

    private static Future<RecordMetadata> produce(Properties properties,
                                                  ProducerRecord<byte[], byte[]> producerRecord) {
        try (var producer = new KafkaProducer<byte[], byte[]>(properties)) {
            return producer.send(producerRecord);
        }
    }

    private Properties getProperties(KafkaProducerDto kafkaProducerEntity,
                                     Map<String, String> additionalProperties) {

        var properties = new Properties();

        var staticProperties = kafkaProducerEntity.getProperties();
        properties.putAll(staticProperties);

        var bootstrapServers = kafkaProducerEntity.getBootstrapServers();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class.getName());

        if (additionalProperties != null) {
            properties.putAll(additionalProperties);
        }
        return properties;
    }

}
