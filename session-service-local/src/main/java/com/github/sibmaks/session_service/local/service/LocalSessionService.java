package com.github.sibmaks.session_service.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error_service.exception.ServiceException;
import com.github.sibmaks.session_service.SessionService;
import com.github.sibmaks.session_service.api.Session;
import com.github.sibmaks.session_service.api.SessionErrors;
import com.github.sibmaks.session_service.api.SessionOwnerType;
import com.github.sibmaks.session_service.local.dto.LocalSession;
import com.github.sibmaks.session_service.local.dto.LocalSessionConfig;
import com.github.sibmaks.session_service.local.dto.LocalSessionSystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class LocalSessionService implements SessionService {
    @Value("${app.session.system_code}")
    private String systemCode;

    private final List<String> readonlySections;
    private final Map<String, LocalSessionSystemConfig> systemConfigMap;
    private final Cache sessions;

    @Autowired
    public LocalSessionService(Resource configResource, ObjectMapper objectMapper) throws IOException {
        var sessionConfig = objectMapper.readValue(configResource.getInputStream(), LocalSessionConfig.class);
        this.readonlySections = sessionConfig.getReadonlySections();
        this.systemConfigMap = sessionConfig.getSystemConfigs().stream()
                .collect(Collectors.toMap(LocalSessionSystemConfig::getSystemCode, it -> it));
        var concurrentMapCacheManager = new ConcurrentMapCacheManager("sessions");
        this.sessions = concurrentMapCacheManager.getCache("sessions");
    }

    @Override
    public Session get(String sessionId) {
        return getLocalSession(sessionId);
    }

    @Override
    public <T extends Serializable> T getAttribute(String sessionId, String section, String attribute) {
        var localSession = getLocalSession(sessionId);
        var systemConfig = systemConfigMap.get(systemCode);
        var sharedSections = systemConfig.getSharedSections();
        if(sharedSections.contains(section)) {
            var shared = localSession.getShared();
            return (T) Optional.ofNullable(shared)
                    .map(it -> it.get(section))
                    .map(it -> it.get(attribute))
                    .orElse(null);
        }
        var local = localSession.getLocal();
        return (T) Optional.ofNullable(local)
                .map(it -> it.get(section))
                .map(it -> it.get(attribute))
                .orElse(null);
    }

    @Override
    public <T extends Serializable> void putAttribute(String sessionId, String section, String attribute, T value) {
        var localSession = getLocalSession(sessionId);
        if(readonlySections.contains(section)) {
            throw new ServiceException(SessionErrors.READONLY, "Section %s is readonly".formatted(section));
        }
        var systemConfig = systemConfigMap.get(systemCode);
        var sharedSections = systemConfig.getSharedSections();
        if(sharedSections.contains(section)) {
            var shared = localSession.getShared();
            putAttribute(section, attribute, value, shared);
            sessions.put(sessionId, localSession);
            return;
        }
        var local = localSession.getLocal();
        putAttribute(section, attribute, value, local);
        sessions.put(sessionId, localSession);
    }

    private static <T extends Serializable> void putAttribute(String section,
                                                              String attribute,
                                                              T value,
                                                              Map<String, Map<String, Serializable>> sections) {
        var sectionAttributes = Optional.ofNullable(sections.get(section)).orElseGet(HashMap::new);
        sectionAttributes.put(attribute, value);
        sections.put(section, sectionAttributes);
    }

    @Override
    public void removeAttribute(String sessionId, String section, String attribute) {
        var localSession = getLocalSession(sessionId);
        if(readonlySections.contains(section)) {
            throw new ServiceException(SessionErrors.READONLY, "Section %s is readonly".formatted(section));
        }
        var systemConfig = systemConfigMap.get(systemCode);
        var sharedSections = systemConfig.getSharedSections();
        if(sharedSections.contains(section)) {
            var shared = localSession.getShared();
            removeAttribute(section, attribute, shared);
            sessions.put(sessionId, localSession);
            return;
        }
        var local = localSession.getLocal();
        removeAttribute(section, attribute, local);
        sessions.put(sessionId, localSession);
    }

    private static <T extends Serializable> void removeAttribute(String section,
                                                              String attribute,
                                                              Map<String, Map<String, Serializable>> sections) {
        var sectionAttributes = Optional.ofNullable(sections.get(section)).orElseGet(HashMap::new);
        sectionAttributes.remove(attribute);
        sections.put(section, sectionAttributes);
    }

    private LocalSession getLocalSession(String sessionId) {
        if(sessions == null) {
            throw new IllegalStateException("session cache not found");
        }
        var localSession = sessions.get(sessionId, LocalSession.class);

        if(localSession == null) {
            throw new ServiceException(SessionErrors.NOT_EXISTS, "Session doesn't exists");
        }
        return localSession;
    }

    public Session create(Map<String, Map<String, Serializable>> sections,
                          SessionOwnerType ownerType,
                          String ownerId,
                          List<String> permissions) {
        var sessionId = UUID.randomUUID().toString();
        var localSession = LocalSession.builder()
                .id(sessionId)
                .shared(new HashMap<>(sections))
                .local(new HashMap<>())
                .ownerType(ownerType)
                .ownerId(ownerId)
                .permissions(new HashSet<>(permissions))
                .build();
        sessions.put(sessionId, localSession);
        return localSession;
    }
}
