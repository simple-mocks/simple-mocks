package com.github.simple_mocks.app.api.rs;

import com.github.simple_mocks.app.api.dto.MockDto;
import com.github.simple_mocks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class GetMockRs extends StandardRs<MockDto> {
    public GetMockRs(MockDto mockDto) {
        super(true, mockDto);
    }
}
