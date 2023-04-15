package com.github.sibmaks.session_service.local.api.dto;

import com.github.sibmaks.session_service.api.dto.SessionOwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sibmaks
 * @since 2023-04-14
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionContentDto implements Serializable {
    private final String sessionId;
    private final Map<String, Map<String, Serializable>> sections;
    private final SessionOwnerType ownerType;
    private final String ownerId;
    private final Set<String> permissions;
}
