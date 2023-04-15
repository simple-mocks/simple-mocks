package com.github.sibmaks.session_service.local.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetSessionRq implements Serializable {
    private String sessionId;
    private Map<String, Map<String, Serializable>> sections;
}
