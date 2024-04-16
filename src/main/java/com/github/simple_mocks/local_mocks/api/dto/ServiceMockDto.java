package com.github.simple_mocks.local_mocks.api.dto;

import com.github.simple_mocks.local_mocks.entity.HttpMockEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Builder
@AllArgsConstructor
public class ServiceMockDto implements Serializable {
    private long mockId;
    private String method;
    private String pathRegex;
    private String type;

    public ServiceMockDto(HttpMockEntity httpMockEntity) {
        this.mockId = httpMockEntity.getId();
        this.method = httpMockEntity.getMethod();
        this.pathRegex = httpMockEntity.getPathRegex();
        this.type = httpMockEntity.getType();
    }
}
