package com.github.sibmaks.session_service.api.dto.action;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-14
 */
@Getter
public final class SetAction extends Action {
    private final Serializable attributeValue;

    public SetAction(String section, String attributeName, Serializable attributeValue) {
        super(section, attributeName);
        this.attributeValue = attributeValue;
    }
}
