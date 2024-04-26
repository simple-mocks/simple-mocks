package com.github.simple_mocks.app.service.handler.impl.js;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simple_mocks.app.entity.HttpMockEntity;
import com.github.simple_mocks.app.service.handler.RequestHandler;
import com.github.simple_mocks.app.service.handler.impl.js.dto.JsRequest;
import com.github.simple_mocks.app.service.handler.impl.js.dto.JsResponse;
import com.github.simple_mocks.app.service.handler.impl.js.dto.JsSessions;
import com.github.simple_mocks.app.service.handler.impl.js.dto.SimleMocksContext;
import com.github.simple_mocks.session.api.SessionService;
import com.github.simple_mocks.storage.api.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class JavaScriptRequestHandler implements RequestHandler {
    private final StorageService storageService;
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    @Autowired
    public JavaScriptRequestHandler(StorageService storageService,
                                    SessionService sessionService,
                                    ObjectMapper objectMapper) {
        this.storageService = storageService;
        this.sessionService = sessionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getType() {
        return "JS";
    }

    @Override
    @SneakyThrows
    public void handle(String path,
                       HttpMockEntity httpMockEntity,
                       HttpServletRequest rq,
                       HttpServletResponse rs) {
        var storageId = httpMockEntity.getStorageId();
        var content = storageService.get(storageId);
        var meta = content.getMeta();
        fillHeaders(rs, meta);

        var simpleMocks = SimleMocksContext.builder()
                .request(new JsRequest(path, rq))
                .response(new JsResponse(objectMapper, rs))
                .sessions(new JsSessions(sessionService))
                .build();

        try (var js = Context.newBuilder("js")
                .allowHostAccess(HostAccess.ALL)
                .build()) {
            js.getBindings("js").putMember("sm", simpleMocks);
            var script = new String(content.getContent(), StandardCharsets.UTF_8);
            try {
                js.eval("js", script);
            } catch (Exception e) {
                log.error("Template execution exception", e);
            }
        }
    }

    private void fillHeaders(HttpServletResponse rs, Map<String, String> meta) throws JsonProcessingException {
        var headersJson = meta.get("HTTP_HEADERS");
        if (headersJson == null) {
            return;
        }
        Map<String, String> headers = objectMapper.readValue(headersJson, Map.class);
        for (var entry : headers.entrySet()) {
            rs.setHeader(entry.getKey(), entry.getValue());
        }

        var statusCode = meta.get("STATUS_CODE");
        if (statusCode != null) {
            var status = Integer.parseInt(statusCode);
            rs.setStatus(status);
        }
    }
}
