package com.github.sibmaks.storage_service.local.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SerializedContent {
    private String id;
    private Map<String, String> meta;
    private String content;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

}
