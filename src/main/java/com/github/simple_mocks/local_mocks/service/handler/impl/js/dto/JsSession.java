package com.github.simple_mocks.local_mocks.service.handler.impl.js.dto;

import com.github.simple_mocks.session.api.ModificationQueryBuilder;
import com.github.simple_mocks.session.api.SessionService;
import com.github.simple_mocks.session.api.dto.Session;
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
    private final Session session;

    @HostAccess.Export
    public Serializable get(String section, String attribute) {
        var sessionId = session.getId();
        return sessionService.getAttribute(sessionId, section, attribute);
    }

    @HostAccess.Export
    public void add(String section, String attribute, Serializable data) {
        var sessionId = session.getId();
        var modificationQuery = ModificationQueryBuilder.builder()
                .create(section, attribute, data)
                .build();
        sessionService.update(sessionId, modificationQuery);
    }

    @HostAccess.Export
    public void set(String section, String attribute, Serializable data) {
        var sessionId = session.getId();
        var modificationQuery = ModificationQueryBuilder.builder()
                .change(section, attribute, data)
                .build();
        sessionService.update(sessionId, modificationQuery);
    }

    @HostAccess.Export
    public void remove(String section, String attribute) {
        var sessionId = session.getId();
        var modificationQuery = ModificationQueryBuilder.builder()
                .remove(section, attribute)
                .build();
        sessionService.update(sessionId, modificationQuery);
    }
}
