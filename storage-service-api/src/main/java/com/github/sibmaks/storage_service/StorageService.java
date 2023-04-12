package com.github.sibmaks.storage_service;

import com.github.sibmaks.storage_service.api.Content;

import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface StorageService {
    /**
     * Storage type code. Must be unique in spring context. <br/>
     * Beware of changing it, if some data already presented in DataBase.
     *
     * @return storage type code
     */
    String getStorageCode();

    /**
     * Get content from content store
     *
     * @param id content id
     * @return content
     */
    Content get(String id);

    /**
     * Create content in content store
     *
     * @param meta meta attributes of content
     * @param content content as binary array
     * @return content id
     */
    String create(Map<String, String> meta, byte[] content);

}
