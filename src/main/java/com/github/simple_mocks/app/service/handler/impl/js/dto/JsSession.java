package com.github.simple_mocks.app.service.handler.impl.js.dto;

import com.github.simple_mocks.session.api.ModificationQueryBuilder;
import com.github.simple_mocks.session.api.SessionId;
import com.github.simple_mocks.session.api.SessionService;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@AllArgsConstructor
public class JsSession {
    private final SessionService sessionService;
    private SessionId sessionId;

    @HostAccess.Export
    public SessionId getId() {
        return sessionId;
    }

    @HostAccess.Export
    public Serializable get(String section, String attribute) {
        return sessionService.getAttribute(sessionId, section, attribute);
    }

    @HostAccess.Export
    public void add(String section, String attribute, Serializable data) {
        var modificationQuery = ModificationQueryBuilder.builder()
                .create(section, attribute, data)
                .build();
        sessionId = sessionService.update(sessionId, modificationQuery);
    }

    @HostAccess.Export
    public void set(String section, String attribute, Serializable data) {
        var modificationQuery = ModificationQueryBuilder.builder()
                .createOrChange(section, attribute, data)
                .build();
        sessionId = sessionService.update(sessionId, modificationQuery);
    }

    @HostAccess.Export
    public void remove(String section, String attribute) {
        var modificationQuery = ModificationQueryBuilder.builder()
                .remove(section, attribute)
                .build();
        sessionId = sessionService.update(sessionId, modificationQuery);
    }
}
