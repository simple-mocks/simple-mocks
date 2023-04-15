package com.github.sibmaks.session_service.local.controller;

import com.github.sibmaks.common.api.rs.StandardRs;
import com.github.sibmaks.session_service.api.dto.action.Action;
import com.github.sibmaks.session_service.api.dto.action.DeleteAction;
import com.github.sibmaks.session_service.api.dto.action.SetAction;
import com.github.sibmaks.session_service.local.api.dto.SessionContentDto;
import com.github.sibmaks.session_service.local.api.dto.SessionDto;
import com.github.sibmaks.session_service.local.api.rq.CreateSessionRq;
import com.github.sibmaks.session_service.local.api.rq.GetSessionRq;
import com.github.sibmaks.session_service.local.api.rq.SetSessionRq;
import com.github.sibmaks.session_service.local.api.rs.CreateSessionRs;
import com.github.sibmaks.session_service.local.api.rs.GetSessionRs;
import com.github.sibmaks.session_service.local.conf.LocalSessionServiceEnabled;
import com.github.sibmaks.session_service.local.service.LocalSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Slf4j
@Controller
@RequestMapping("${app.local.uri.session.path}")
@ConditionalOnBean(LocalSessionServiceEnabled.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionController {
    private final LocalSessionService sessionService;

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CreateSessionRs create(@RequestBody CreateSessionRq rq) {
        var session = sessionService.create(rq.getSections(),
                rq.getOwnerType(),
                rq.getOwnerId(),
                rq.getPermissions());
        var sessionDto = SessionDto.builder()
                .id(session.getId())
                .ownerId(session.getOwnerId())
                .ownerType(session.getOwnerType())
                .permissions(session.getPermissions())
                .sections(session.getSections())
                .build();
        return new CreateSessionRs(sessionDto);
    }

    @PostMapping(value = "/get",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GetSessionRs get(@RequestBody GetSessionRq rq) {
        var sessionId = rq.getSessionId();
        var session = sessionService.get(sessionId);
        var sections = new HashMap<String, Map<String, Serializable>>();
        for (var section : session.getSections()) {
            var attributeNames = sessionService.getAttributeNames(sessionId, section);
            var sectionAttributes = new HashMap<String, Serializable>();
            sections.put(section, sectionAttributes);
            for (var attributeName : attributeNames) {
                var attributeValue = sessionService.getAttribute(sessionId, section, attributeName);
                sectionAttributes.put(attributeName, attributeValue);
            }
        }
        var id = session.getId();
        var ownerType = session.getOwnerType();
        var ownerId = session.getOwnerId();
        var permissions = session.getPermissions();
        var sessionContent = SessionContentDto.builder()
                .sessionId(id)
                .sections(sections)
                .ownerId(ownerId)
                .ownerType(ownerType)
                .permissions(permissions)
                .build();
        return new GetSessionRs(sessionContent);
    }

    @PostMapping(value = "/set",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody StandardRs<?> set(@RequestBody SetSessionRq rq) {
        var sessionId = rq.getSessionId();
        var sections = rq.getSections();

        var actions = new ArrayList<Action>();
        for (var entry : sections.entrySet()) {
            var section = entry.getKey();
            var attributes = entry.getValue();
            for (var attributeEntry : attributes.entrySet()) {
                var attributeName = attributeEntry.getKey();
                var attributeValue = attributeEntry.getValue();
                if(attributeValue == null) {
                    var action = new DeleteAction(section, attributeName);
                    actions.add(action);
                } else {
                    var action = new SetAction(section, attributeName, attributeValue);
                    actions.add(action);
                }
            }
        }

        sessionService.update(sessionId, actions);
        return new StandardRs<>();
    }

}
