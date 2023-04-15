package com.github.sibmaks.session_service.api.dto;

import java.util.Set;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
public interface Session {
    /**
     * Session identifier
     *
     * @return session id
     */
    String getId();

    /**
     * Session owner type.<br/>
     * Session can belong to user or service
     *
     * @return session owner type
     */
    SessionOwnerType getOwnerType();

    /**
     * Session owner id.<br/>
     * Unique identifier of the owner in the cut {@link SessionOwnerType}
     *
     * @return session owner id
     */
    String getOwnerId();

    /**
     * Session sections, that current service allow to access
     *
     * @return set of sections
     */
    Set<String> getSections();

    /**
     * Session permissions list
     *
     * @return set of permissions
     */
    Set<String> getPermissions();
}
