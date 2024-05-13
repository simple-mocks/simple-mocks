package com.github.simple_mocks.local_mocks.api.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.local_mocks.api.dto.ServiceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServicesRs extends StandardRs<ArrayList<ServiceDto>> {
    public GetServicesRs(List<ServiceDto> serviceDto) {
        super(new ArrayList<>(serviceDto));
    }
}
