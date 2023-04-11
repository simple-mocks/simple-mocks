package com.github.sibmaks.storage_service.api;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface Content {
    String getId();

    Map<String, String> getMeta();

    byte[] getContent();

    ZonedDateTime getCreatedAt();

    ZonedDateTime getModifiedAt();
}
