package com.github.sibmaks.local_mocks.service.storage;

import com.github.sibmaks.storage_service.api.Content;
import com.github.sibmaks.storage_service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Service
public class StorageFacadeService {
    private final Map<String, StorageService> storageServices;

    @Autowired
    public StorageFacadeService(List<StorageService> storageServices) {
        this.storageServices = storageServices.stream()
                .collect(Collectors.toMap(StorageService::getStorageCode, it -> it));
    }

    public Content get(String storageCode, String id) {
        StorageService storageService = getStorageService(storageCode);
        return storageService.get(id);
    }

    public String create(String storageCode, Map<String, String> meta, byte[] content) {
        StorageService storageService = getStorageService(storageCode);
        return storageService.create(meta, content);
    }

    private StorageService getStorageService(String storageCode) {
        StorageService storageService = storageServices.get(storageCode);
        if(storageService == null) {
            throw new IllegalArgumentException("Storage with code %s not found".formatted(storageCode));
        }
        return storageService;
    }
}
