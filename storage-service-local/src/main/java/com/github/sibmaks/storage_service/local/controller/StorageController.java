package com.github.sibmaks.storage_service.local.controller;

import com.github.sibmaks.storage_service.api.Content;
import com.github.sibmaks.storage_service.local.api.rq.CreateContentRq;
import com.github.sibmaks.storage_service.local.api.rq.GetContentRq;
import com.github.sibmaks.storage_service.local.api.rs.CreateContentRs;
import com.github.sibmaks.storage_service.local.conf.LocalStorageServiceEnabled;
import com.github.sibmaks.storage_service.local.service.LocalStorageService;
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

import java.util.Base64;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Slf4j
@Controller
@RequestMapping("${app.local.uri.storage.path}")
@ConditionalOnBean(LocalStorageServiceEnabled.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StorageController {
    private final LocalStorageService localStorageService;

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CreateContentRs create(@RequestBody CreateContentRq rq) {
        var decoder = Base64.getDecoder();
        var decoded = decoder.decode(rq.getContent());
        var id = localStorageService.create(rq.getMeta(), decoded);
        return new CreateContentRs(id);
    }

    @PostMapping(value = "/get",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Content get(@RequestBody GetContentRq rq) {
        return localStorageService.get(rq.getId());
    }


}
