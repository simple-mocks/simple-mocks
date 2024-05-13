package com.github.simple_mocks.local_mocks.controller;

import com.github.simple_mocks.common.api.rs.StandardEmptyRs;
import com.github.simple_mocks.local_mocks.api.kafka.rq.CreateKafkaProducerRq;
import com.github.simple_mocks.local_mocks.api.kafka.rq.GetKafkaProducerRq;
import com.github.simple_mocks.local_mocks.api.kafka.rq.SendKafkaProducerRq;
import com.github.simple_mocks.local_mocks.api.kafka.rq.UpdateKafkaProducerRq;
import com.github.simple_mocks.local_mocks.api.kafka.rs.GetKafkaProducerRs;
import com.github.simple_mocks.local_mocks.api.kafka.rs.SendKafkaProducerRs;
import com.github.simple_mocks.local_mocks.service.kafka.KafkaFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.kafka.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiKafkaController {

    private final KafkaFacadeService kafkaFacadeService;

    @Autowired
    public ApiKafkaController(KafkaFacadeService kafkaFacadeService) {
        this.kafkaFacadeService = kafkaFacadeService;
    }

    @PostMapping("/producer/create")
    public StandardEmptyRs create(@RequestBody CreateKafkaProducerRq rq) {
        kafkaFacadeService.createProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/producer/update")
    public StandardEmptyRs update(@RequestBody UpdateKafkaProducerRq rq) {
        kafkaFacadeService.updateProducer(
                rq.getCode(),
                rq.getDescription(),
                rq.getBootstrapServers(),
                rq.getProperties()
        );
        return new StandardEmptyRs();
    }

    @PostMapping("/producer/get")
    public GetKafkaProducerRs get(@RequestBody GetKafkaProducerRq rq) {
        var dto = kafkaFacadeService.getProducer(
                rq.getCode()
        );
        return new GetKafkaProducerRs(dto);
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
