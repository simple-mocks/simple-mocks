package com.github.simple_mocks.local_mocks.api.kafka.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class SendKafkaProducerRs extends StandardRs<Boolean> {

    public SendKafkaProducerRs(Boolean done) {
        super(done);
    }

}
