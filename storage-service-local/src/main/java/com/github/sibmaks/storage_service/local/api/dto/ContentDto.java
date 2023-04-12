package com.github.sibmaks.storage_service.local.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Content dto
 *
 * @author sibmaks
 * @since 2023-04-12
 */
@Builder
public record ContentDto(String id,
                         Map<String, String> meta,
                         String content,
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZZZZZ") ZonedDateTime createdAt,
                         @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZZZZZ") ZonedDateTime modifiedAt)
        implements Serializable {
}
