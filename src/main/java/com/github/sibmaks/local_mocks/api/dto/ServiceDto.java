package com.github.sibmaks.local_mocks.api.dto;

import com.github.sibmaks.local_mocks.entity.HttpMockEntity;
import com.github.sibmaks.local_mocks.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
@Getter
@AllArgsConstructor
public class ServiceDto implements Serializable {
    private long serviceId;
    private String code;
    private List<ServiceMockDto> serviceMockDtos;

    public ServiceDto(ServiceEntity serviceEntity, List<HttpMockEntity> httpMockEntities) {
        this.serviceId = serviceEntity.getId();
        this.code = serviceEntity.getCode();
        this.serviceMockDtos = httpMockEntities.stream()
                .map(it -> ServiceMockDto.builder()
                        .mockId(it.getId())
                        .method(it.getMethod())
                        .pathRegex(it.getPathRegex())
                        .type(it.getType())
                        .build())
                .collect(Collectors.toList());
    }
}
