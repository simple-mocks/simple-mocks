package com.github.simple_mocks.app.api.dto;

import com.github.simple_mocks.app.entity.HttpMockEntity;
import com.github.simple_mocks.app.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class ServiceDto implements Serializable {
    private long serviceId;
    private String code;
    private List<ServiceMockDto> mocks;

    public ServiceDto(ServiceEntity serviceEntity, List<HttpMockEntity> httpMockEntities) {
        this.serviceId = serviceEntity.getId();
        this.code = serviceEntity.getCode();
        this.mocks = httpMockEntities.stream()
                .map(it -> ServiceMockDto.builder()
                        .mockId(it.getId())
                        .method(it.getMethod())
                        .pathRegex(it.getPathRegex())
                        .type(it.getType())
                        .build())
                .toList();
    }
}
