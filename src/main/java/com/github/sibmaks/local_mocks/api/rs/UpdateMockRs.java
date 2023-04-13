package com.github.sibmaks.local_mocks.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
public class UpdateMockRs extends StandardRs<Long> {
    public UpdateMockRs(long id) {
        super(true, id);
    }
}
