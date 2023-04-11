package com.github.sibmaks.local_mocks.service.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.local_mocks.entity.HttpMockEntity;
import com.github.sibmaks.local_mocks.service.handler.RequestHandler;
import com.github.sibmaks.local_mocks.service.storage.StorageFacadeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaticRequestHandler implements RequestHandler {
    private final StorageFacadeService storageFacadeService;
    private final ObjectMapper objectMapper;

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
        var storageType = httpMockEntity.getStorageType();
        var storageId = httpMockEntity.getStorageId();
        var content = storageFacadeService.get(storageType, storageId);
        var meta = content.getMeta();
        fillHeaders(rs, meta);
        var outputStream = rs.getOutputStream();
        outputStream.write(content.getContent());
    }

    private void fillHeaders(HttpServletResponse rs, Map<String, String> meta) throws JsonProcessingException {
        String headersJson = meta.get("HTTP_HEADERS");
        Map<String, String> headers = objectMapper.readValue(headersJson, Map.class);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            rs.setHeader(entry.getKey(), entry.getValue());
        }

        var statusCode = meta.get("STATUS_CODE");
        if(statusCode != null) {
            int status = Integer.parseInt(statusCode);
            rs.setStatus(status);
        }
    }
}
