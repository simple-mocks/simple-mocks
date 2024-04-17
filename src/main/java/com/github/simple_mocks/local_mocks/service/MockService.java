package com.github.simple_mocks.local_mocks.service;

import com.github.simple_mocks.local_mocks.api.dto.MockDto;
import com.github.simple_mocks.local_mocks.api.dto.ServiceDto;
import com.github.simple_mocks.local_mocks.entity.HttpMockEntity;
import com.github.simple_mocks.local_mocks.entity.ServiceEntity;
import com.github.simple_mocks.local_mocks.repository.HttpMockEntityRepository;
import com.github.simple_mocks.local_mocks.repository.ServiceEntityRepository;
import com.github.simple_mocks.local_mocks.service.handler.RequestHandler;
import com.github.simple_mocks.storage.api.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Service
public class MockService {
    private final Set<String> mockTypes;
    private final ServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;

    public MockService(List<RequestHandler> handlers,
                       ServiceEntityRepository serviceEntityRepository,
                       HttpMockEntityRepository httpMockEntityRepository,
                       StorageService storageService) {
        this.mockTypes = handlers.stream()
                .map(RequestHandler::getType)
                .collect(Collectors.toSet());
        this.serviceEntityRepository = serviceEntityRepository;
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.storageService = storageService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public HttpMockEntity create(long serviceId,
                                 String method,
                                 String pathRegex,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        try {
            Pattern.compile(pathRegex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Not valid path regex passed: '%s'".formatted(pathRegex), e);
        }
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        meta = meta == null ? Collections.emptyMap() : meta;
        var contentName = "%s-%s-%s".formatted(method, serviceEntity.getCode(), type);
        var contentId = storageService.create("mocks", contentName, meta, content);

        var httpMockEntity = HttpMockEntity.builder()
                .method(method)
                .pathRegex(pathRegex)
                .service(serviceEntity)
                .type(type)
                .storageType("LOCAL")
                .storageId(contentId)
                .createdAt(new Date())
                .build();
        return httpMockEntityRepository.save(httpMockEntity);
    }

    @Transactional
    public HttpMockEntity update(long mockId,
                                 String method,
                                 String pathRegex,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if (!mockTypes.contains(type)) {
            throw new IllegalArgumentException("Type %s not supported".formatted(type));
        }
        try {
            Pattern.compile(pathRegex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Not valid path regex passed: '%s'".formatted(pathRegex), e);
        }
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));
        meta = meta == null ? Collections.emptyMap() : meta;
        var contentName = "%s-%s".formatted(meta, type);
        var contentId = storageService.create("mocks", contentName, meta, content);

        httpMockEntity.setMethod(method);
        httpMockEntity.setPathRegex(pathRegex);
        httpMockEntity.setType(type);
        httpMockEntity.setStorageType("LOCAL");
        httpMockEntity.setStorageId(contentId);

        return httpMockEntityRepository.save(httpMockEntity);
    }

    public MockDto get(long mockId) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));
        var storageId = httpMockEntity.getStorageId();
        var content = storageService.get(storageId);
        return new MockDto(httpMockEntity, content);
    }

    public ServiceEntity createService(String code) {
        var serviceEntity = ServiceEntity.builder()
                .code(code)
                .createdAt(new Date())
                .build();
        return serviceEntityRepository.save(serviceEntity);
    }

    public ServiceDto getService(long serviceId) {
        var serviceEntity = serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service %s not found".formatted(serviceId)));
        var httpMockEntities = httpMockEntityRepository.findAllByServiceId(serviceId);
        return new ServiceDto(serviceEntity, httpMockEntities);
    }

    public List<ServiceDto> getAllServices() {
        return serviceEntityRepository.findAll()
                .stream()
                .map(it -> {
                    var mocks = httpMockEntityRepository.findAllByServiceId(it.getId());
                    return new ServiceDto(it, mocks);
                })
                .toList();
    }
}
