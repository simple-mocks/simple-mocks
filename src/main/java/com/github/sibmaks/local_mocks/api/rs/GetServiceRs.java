package com.github.sibmaks.local_mocks.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.local_mocks.api.dto.ServiceDto;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
public class GetServiceRs extends StandardRs<ServiceDto> {
    public GetServiceRs(ServiceDto serviceDto) {
        super(true, serviceDto);
    }
}
