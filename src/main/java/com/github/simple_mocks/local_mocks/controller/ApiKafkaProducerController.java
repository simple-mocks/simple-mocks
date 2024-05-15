package com.github.simple_mocks.local_mocks.controller;

import com.github.simple_mocks.common.api.rs.StandardEmptyRs;
import com.github.simple_mocks.local_mocks.api.kafka.rq.*;
import com.github.simple_mocks.local_mocks.api.kafka.rs.GetKafkaProducerRs;
import com.github.simple_mocks.local_mocks.api.kafka.rs.GetKafkaProducersRs;
import com.github.simple_mocks.local_mocks.api.kafka.rs.SendKafkaProducerRs;
import com.github.simple_mocks.local_mocks.service.kafka.KafkaProducerDaService;
import com.github.simple_mocks.local_mocks.service.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.kafka.path}/producer/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiKafkaProducerController {

    private final KafkaProducerDaService kafkaProducerDaService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public ApiKafkaProducerController(KafkaProducerDaService kafkaProducerDaService,
                                      KafkaProducerService kafkaProducerService) {
        this.kafkaProducerDaService = kafkaProducerDaService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/create")
    public StandardEmptyRs createProducer(@RequestBody CreateKafkaProducerRq rq) {
        kafkaProducerDaService.createProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/update")
    public StandardEmptyRs updateProducer(@RequestBody UpdateKafkaProducerRq rq) {
        kafkaProducerDaService.updateProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/delete")
    public StandardEmptyRs deleteProducer(@RequestBody DeleteKafkaProducerRq rq) {
        kafkaProducerDaService.deleteProducer(
                rq.getCode()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/get")
    public GetKafkaProducerRs getProducer(@RequestBody GetKafkaProducerRq rq) {
        var dto = kafkaProducerDaService.getProducer(
                rq.getCode()
        );
        return new GetKafkaProducerRs(dto);
    }

    @GetMapping("/getAll")
    public GetKafkaProducersRs getProducers() {
        var dtos = kafkaProducerDaService.getProducers();
        return new GetKafkaProducersRs(dtos);
    }

    @PostMapping("/send")
    public SendKafkaProducerRs send(@RequestBody SendKafkaProducerRq rq) {
        var done = kafkaProducerService.produce(
                rq.getCode(),
                rq.getAdditionalProperties(),
                rq.getTopic(),
                rq.getPartition(),
                rq.getTimestamp(),
                rq.getKey(),
                rq.getValue(),
                rq.getHeaders()
        );
        return new SendKafkaProducerRs(done);
    }


}
