package com.github.sibmaks.session_service.local.api.dto;

import com.github.sibmaks.session_service.api.dto.Session;
import com.github.sibmaks.session_service.api.dto.SessionOwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author sibmaks
 * @since 2023-04-14
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionDto implements Session, Serializable {
    private String id;
    private SessionOwnerType ownerType;
    private String ownerId;
    private Set<String> sections;
    private Set<String> permissions;
}
