package com.github.simple_mocks.app.api.dto;

import com.github.simple_mocks.app.entity.HttpMockEntity;
import com.github.simple_mocks.storage.api.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class MockDto implements Serializable {
    private long serviceId;
    private long mockId;
    private String method;
    private String pathRegex;
    private String type;
    private Map<String, String> meta;
    private String content;

    public MockDto(HttpMockEntity httpMockEntity, Content content) {
        var service = httpMockEntity.getService();
        this.serviceId = service.getId();
        this.mockId = httpMockEntity.getId();
        this.method = httpMockEntity.getMethod();
        this.pathRegex = httpMockEntity.getPathRegex();
        this.type = httpMockEntity.getType();
        this.meta = content.getMeta();
        this.content = Base64.getEncoder().encodeToString(content.getContent());
    }
}
