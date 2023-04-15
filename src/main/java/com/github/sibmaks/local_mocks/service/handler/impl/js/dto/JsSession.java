package com.github.sibmaks.local_mocks.service.handler.impl.js.dto;

import com.github.sibmaks.session_service.SessionService;
import com.github.sibmaks.session_service.api.dto.Session;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
@AllArgsConstructor
public class JsSession {
    private final SessionService sessionService;
    private final Session session;

    @HostAccess.Export
    public Serializable get(String section, String attribute) {
        String sessionId = session.getId();
        return sessionService.getAttribute(sessionId, section, attribute);
    }

    @HostAccess.Export
    public void set(String section, String attribute, Serializable data) {
        String sessionId = session.getId();
        sessionService.putAttribute(sessionId, section, attribute, data);
    }

    @HostAccess.Export
    public void remove(String section, String attribute) {
        String sessionId = session.getId();
        sessionService.removeAttribute(sessionId, section, attribute);
    }
}
