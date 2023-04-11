package com.github.sibmaks.storage_service;

import com.github.sibmaks.storage_service.api.Content;

import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface StorageService {

    String getStorageCode();

    Content get(String id);

    String create(Map<String, String> meta, byte[] content);

}
