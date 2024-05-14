package com.github.simple_mocks.local_mocks.controller;

import com.github.simple_mocks.common.api.rs.StandardEmptyRs;
import com.github.simple_mocks.local_mocks.api.kafka.rq.*;
import com.github.simple_mocks.local_mocks.api.kafka.rs.GetKafkaProducerRs;
import com.github.simple_mocks.local_mocks.api.kafka.rs.GetKafkaProducersRs;
import com.github.simple_mocks.local_mocks.api.kafka.rs.SendKafkaProducerRs;
import com.github.simple_mocks.local_mocks.service.kafka.KafkaDaService;
import com.github.simple_mocks.local_mocks.service.kafka.KafkaFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.kafka.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiKafkaController {

    private final KafkaDaService kafkaDaService;
    private final KafkaFacadeService kafkaFacadeService;

    @Autowired
    public ApiKafkaController(KafkaDaService kafkaDaService,
                              KafkaFacadeService kafkaFacadeService) {
        this.kafkaDaService = kafkaDaService;
        this.kafkaFacadeService = kafkaFacadeService;
    }

    @PostMapping("/producer/create")
    public StandardEmptyRs createProducer(@RequestBody CreateKafkaProducerRq rq) {
        kafkaDaService.createProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/producer/update")
    public StandardEmptyRs updateProducer(@RequestBody UpdateKafkaProducerRq rq) {
        kafkaDaService.updateProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/producer/delete")
    public StandardEmptyRs deleteProducer(@RequestBody DeleteKafkaProducerRq rq) {
        kafkaDaService.deleteProducer(
                rq.getCode()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/producer/get")
    public GetKafkaProducerRs getProducer(@RequestBody GetKafkaProducerRq rq) {
        var dto = kafkaDaService.getProducer(
                rq.getCode()
        );
        return new GetKafkaProducerRs(dto);
    }

    @GetMapping("/producer/getAll")
    public GetKafkaProducersRs getProducers() {
        var dtos = kafkaDaService.getProducers();
        return new GetKafkaProducersRs(dtos);
    }

    @PostMapping("/producer/send")
    public SendKafkaProducerRs send(@RequestBody SendKafkaProducerRq rq) {
        var done = kafkaFacadeService.produce(
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
