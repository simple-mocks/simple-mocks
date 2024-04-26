package com.github.simple_mocks.app.service.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.simple_mocks.app.entity.HttpMockEntity;
import com.github.simple_mocks.app.service.handler.RequestHandler;
import com.github.simple_mocks.storage.api.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@Component
public class StaticRequestHandler implements RequestHandler {
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    public StaticRequestHandler(StorageService storageService,
                                ObjectMapper objectMapper) {
        this.storageService = storageService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getType() {
        return "STATIC";
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
        var outputStream = rs.getOutputStream();
        outputStream.write(content.getContent());
    }

    private void fillHeaders(HttpServletResponse rs, Map<String, String> meta) {
        var headersJson = meta.get("HTTP_HEADERS");
        if (headersJson != null && !headersJson.isBlank()) {
            try {
                Map<String, String> headers = objectMapper.readValue(headersJson, Map.class);
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    rs.setHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                log.error("Can't set all http headers", e);
            }
        }

        var statusCode = meta.get("STATUS_CODE");
        if (statusCode != null) {
            int status = Integer.parseInt(statusCode);
            rs.setStatus(status);
        }
    }
}
