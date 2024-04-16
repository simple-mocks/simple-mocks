package com.github.simple_mocks.local_mocks.service.handler.impl.js.dto;

import com.github.simple_mocks.session.api.SessionService;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.1
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
