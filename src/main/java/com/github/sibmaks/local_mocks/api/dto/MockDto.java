package com.github.sibmaks.local_mocks.api.dto;

import com.github.sibmaks.local_mocks.entity.HttpMockEntity;
import com.github.sibmaks.storage_service.api.Content;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
@Getter
@AllArgsConstructor
public class MockDto implements Serializable {
    private long mockId;
    private String method;
    private String pathRegex;
    private String type;
    private Map<String, String> meta;
    private String content;

    public MockDto(HttpMockEntity httpMockEntity, Content content) {
        this.mockId = httpMockEntity.getId();
        this.method = httpMockEntity.getMethod();
        this.pathRegex = httpMockEntity.getPathRegex();
        this.type = httpMockEntity.getType();
        this.meta = content.getMeta();
        this.content = Base64.getEncoder().encodeToString(content.getContent());
    }
}
