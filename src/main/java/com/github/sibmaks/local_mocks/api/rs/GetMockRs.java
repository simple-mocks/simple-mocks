package com.github.sibmaks.local_mocks.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.local_mocks.api.dto.MockDto;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
public class GetMockRs extends StandardRs<MockDto> {
    public GetMockRs(MockDto mockDto) {
        super(true, mockDto);
    }
}
