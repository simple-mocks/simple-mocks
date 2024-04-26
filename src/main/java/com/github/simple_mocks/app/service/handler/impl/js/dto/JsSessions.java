package com.github.simple_mocks.app.service.handler.impl.js.dto;

import com.github.simple_mocks.session.api.SessionId;
import com.github.simple_mocks.session.api.SessionService;
import com.github.simple_mocks.session.api.dto.SessionOwnerType;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@AllArgsConstructor
public class JsSessions {
    private final SessionService sessionService;

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId) {
        var session = sessionService.get(sessionId);
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId, long version) {
        var session = sessionService.get(SessionId.of(sessionId, version));
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId, String versionCode) {
        var version = Long.parseLong(versionCode);
        var session = sessionService.get(SessionId.of(sessionId, version));
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession create(@Nonnull Map<String, Map<String, Serializable>> sections,
                            @Nonnull String ownerTypeCode,
                            @Nonnull String ownerId,
                            @Nonnull List<String> permissions) {
        var ownerType = SessionOwnerType.valueOf(ownerTypeCode);
        var sessionId = sessionService.create(sections, ownerType, ownerId, permissions);
        var session = sessionService.get(sessionId);
        return new JsSession(sessionService, session.getId());
    }

}
