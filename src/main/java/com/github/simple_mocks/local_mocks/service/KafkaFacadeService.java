package com.github.simple_mocks.local_mocks.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaFacadeService {


    public void produce(String topic) {
        String bootstrapServers = "127.0.0.1:9092";

        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        try (var producer = new KafkaProducer<String, String>(properties)) {

            // create a producer record
            var producerRecord = new ProducerRecord<String, String>(topic, "hello world");

            // send data - asynchronous
            producer.send(producerRecord);

            // flush data - synchronous
            producer.flush();
        }
    }

    public static void main(String[] args) {
        KafkaFacadeService kafkaFacadeService = new KafkaFacadeService();
        kafkaFacadeService.produce("test");
    }

}
