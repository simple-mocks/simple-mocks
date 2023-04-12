package com.github.sibmaks.session_service.local.api.rq;

import com.github.sibmaks.session_service.api.SessionOwnerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRq implements Serializable {
    private Map<String, Map<String, Serializable>> sections;
    private SessionOwnerType ownerType;
    private String ownerId;
    private List<String> permissions;
}
