package com.github.simple_mocks.local_mocks.api.kafka.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.local_mocks.api.kafka.dto.KafkaProducerShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class GetKafkaProducersRs extends StandardRs<ArrayList<KafkaProducerShortDto>> {

    public GetKafkaProducersRs(List<KafkaProducerShortDto> producerDto) {
        super(
                Optional.ofNullable(producerDto)
                        .map(ArrayList::new)
                        .orElseGet(ArrayList::new)
        );
    }

}
