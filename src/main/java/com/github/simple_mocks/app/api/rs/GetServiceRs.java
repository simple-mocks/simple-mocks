package com.github.simple_mocks.app.api.rs;

import com.github.simple_mocks.app.api.dto.ServiceDto;
import com.github.simple_mocks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServiceRs extends StandardRs<ServiceDto> {
    public GetServiceRs(ServiceDto serviceDto) {
        super(true, serviceDto);
    }
}
