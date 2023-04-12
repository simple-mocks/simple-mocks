package com.github.sibmaks.session_service.local.controller;

import com.github.sibmaks.session_service.local.api.rq.CreateSessionRq;
import com.github.sibmaks.session_service.local.api.rs.CreateSessionRs;
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
        var id = sessionService.create(rq.getSections(),
                rq.getOwnerType(),
                rq.getOwnerId(),
                rq.getPermissions());
        return new CreateSessionRs(id);
    }

}
