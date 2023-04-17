package com.github.sibmaks.local_mocks.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
public class CreateServiceRs extends StandardRs<Long> {
    public CreateServiceRs(long id) {
        super(true, id);
    }
}
