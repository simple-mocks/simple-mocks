package com.github.sibmaks.local_mocks.service.handler.impl.js.dto;

import com.github.sibmaks.session_service.SessionService;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
@AllArgsConstructor
public class JsSessions {
    private final SessionService sessionService;

    @HostAccess.Export
    public JsSession get(String sessionId) {
        var session = sessionService.get(sessionId);
        return new JsSession(sessionService, session);
    }
}
