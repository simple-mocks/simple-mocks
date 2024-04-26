package com.github.simple_mocks.app.api.rs;

import com.github.simple_mocks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class CreateServiceRs extends StandardRs<Long> {
    public CreateServiceRs(long id) {
        super(true, id);
    }
}
