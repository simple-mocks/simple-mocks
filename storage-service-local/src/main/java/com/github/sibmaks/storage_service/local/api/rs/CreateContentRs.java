package com.github.sibmaks.storage_service.local.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
public class CreateContentRs extends StandardRs<String> {
    public CreateContentRs(String id) {
        super(true, id);
    }
}
