package com.github.sibmaks.storage_service.api;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Definition of type Content.
 *
 * @author sibmaks
 * @since 2023-04-11
 */
public interface Content {
    /**
     * Content id in storage
     *
     * @return content id
     */
    String getId();

    /**
     * Content meta attributes
     *
     * @return meta attributes
     */
    Map<String, String> getMeta();

    /**
     * Content content
     *
     * @return content content
     */
    byte[] getContent();

    /**
     * Date of content creation
     * @return creation date
     */
    ZonedDateTime getCreatedAt();

    /**
     * Date of content last modification
     * @return modification date
     */
    ZonedDateTime getModifiedAt();
}
