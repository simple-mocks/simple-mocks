package com.github.simple_mocks.app.api.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class UpdateMockRs extends StandardRs<Long> {
    public UpdateMockRs(long id) {
        super(true, id);
    }
}
