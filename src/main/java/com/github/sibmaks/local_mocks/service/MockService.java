package com.github.sibmaks.local_mocks.service;

import com.github.sibmaks.local_mocks.api.dto.MockDto;
import com.github.sibmaks.local_mocks.entity.HttpMockEntity;
import com.github.sibmaks.local_mocks.repository.HttpMockEntityRepository;
import com.github.sibmaks.local_mocks.repository.ServiceEntityRepository;
import com.github.sibmaks.local_mocks.service.handler.RequestHandler;
import com.github.sibmaks.local_mocks.service.storage.StorageFacadeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
@Service
public class MockService {
    @Value("${app.mock.save.storage.code}")
    private String saveStorageCode;

    private final Set<String> mockTypes;
    private final ServiceEntityRepository serviceEntityRepository;
    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageFacadeService storageFacadeService;

    public MockService(List<RequestHandler> handlers,
                       ServiceEntityRepository serviceEntityRepository,
                       HttpMockEntityRepository httpMockEntityRepository,
                       StorageFacadeService storageFacadeService) {
        this.mockTypes = handlers.stream()
                .map(RequestHandler::getType)
                .collect(Collectors.toSet());
        this.serviceEntityRepository = serviceEntityRepository;
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.storageFacadeService = storageFacadeService;
    }

    @Transactional
    public HttpMockEntity create(long serviceId,
                                 String method,
                                 String pathRegex,
                                 String type,
                                 Map<String, String> meta,
                                 byte[] content) {
        if(!mockTypes.contains(type)) {
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
        var storageCode = saveStorageCode;
        var contentId = storageFacadeService.create(storageCode, meta, content);

        var httpMockEntity = HttpMockEntity.builder()
                .method(method)
                .pathRegex(pathRegex)
                .service(serviceEntity)
                .type(type)
                .storageType(storageCode)
                .storageId(contentId)
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
        if(!mockTypes.contains(type)) {
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
        var storageCode = saveStorageCode;
        var contentId = storageFacadeService.create(storageCode, meta, content);

        httpMockEntity.setMethod(method);
        httpMockEntity.setPathRegex(pathRegex);
        httpMockEntity.setType(type);
        httpMockEntity.setStorageType(storageCode);
        httpMockEntity.setStorageId(contentId);

        return httpMockEntityRepository.save(httpMockEntity);
    }

    public MockDto get(long mockId) {
        var httpMockEntity = httpMockEntityRepository.findById(mockId)
                .orElseThrow(() -> new IllegalArgumentException("Mock %s not found".formatted(mockId)));
        var content = storageFacadeService.get(httpMockEntity.getStorageType(), httpMockEntity.getStorageId());
        return new MockDto(httpMockEntity, content);
    }
}
