package com.github.sibmaks.session_service.local.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.session_service.local.api.dto.SessionContentDto;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class GetSessionRs extends StandardRs<SessionContentDto> {
    public GetSessionRs(SessionContentDto sessionContentDto) {
        super(true, sessionContentDto);
    }
}
