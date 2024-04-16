package com.github.simple_mocks.local_mocks.api.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.local_mocks.api.dto.MockDto;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetMockRs extends StandardRs<MockDto> {
    public GetMockRs(MockDto mockDto) {
        super(true, mockDto);
    }
}
