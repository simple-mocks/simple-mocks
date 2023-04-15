package com.github.sibmaks.session_service.local.api.rs;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.session_service.local.api.dto.SessionDto;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class CreateSessionRs extends StandardRs<SessionDto> {
    public CreateSessionRs(SessionDto session) {
        super(true, session);
    }
}
