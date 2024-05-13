package com.github.simple_mocks.local_mocks.api.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.local_mocks.api.dto.ServiceDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServiceRs extends StandardRs<ServiceDto> {
    public GetServiceRs(ServiceDto serviceDto) {
        super(serviceDto);
    }
}
