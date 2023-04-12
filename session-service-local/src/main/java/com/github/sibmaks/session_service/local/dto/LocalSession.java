package com.github.sibmaks.session_service.local.dto;

import com.github.sibmaks.session_service.api.Session;
import com.github.sibmaks.session_service.api.SessionOwnerType;
import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalSession implements Session {
    private String id;
    private SessionOwnerType ownerType;
    private String ownerId;
    private Map<String, Map<String, Serializable>> shared;
    private Map<String, Map<String, Serializable>> local;
    private Set<String> permissions;

    @Override
    public Set<String> getSections() {
        var sections = new HashSet<>(shared.keySet());
        sections.addAll(local.keySet());
        return Collections.unmodifiableSet(sections);
    }
}
