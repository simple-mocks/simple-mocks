package com.github.sibmaks.session_service.api.dto.action;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-14
 */
@Getter
@AllArgsConstructor
public sealed class Action implements Serializable permits AddAction, DeleteAction, SetAction {
    private final String section;
    private final String attributeName;
}
