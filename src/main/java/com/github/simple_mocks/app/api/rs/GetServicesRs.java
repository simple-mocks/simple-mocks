package com.github.simple_mocks.app.api.rs;

import com.github.simple_mocks.app.api.dto.ServiceDto;
import com.github.simple_mocks.common.api.rs.StandardRs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetServicesRs extends StandardRs<ArrayList<ServiceDto>> {
    public GetServicesRs(List<ServiceDto> serviceDto) {
        super(true, new ArrayList<>(serviceDto));
    }
}
