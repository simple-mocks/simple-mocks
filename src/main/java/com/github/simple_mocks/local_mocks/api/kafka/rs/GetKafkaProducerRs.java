package com.github.simple_mocks.local_mocks.api.kafka.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerDto;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class GetKafkaProducerRs extends StandardRs<KafkaProducerDto> {

    public GetKafkaProducerRs(KafkaProducerDto producerDto) {
        super(producerDto);
    }

}
