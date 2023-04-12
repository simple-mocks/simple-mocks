package com.github.sibmaks.storage_service.local.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.storage_service.local.api.dto.ContentDto;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class GetContentRs extends StandardRs<ContentDto> {
    public GetContentRs(ContentDto dto) {
        super(true, dto);
    }
}
