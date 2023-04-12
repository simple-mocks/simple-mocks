package com.github.sibmaks.session_service.local.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalSessionSystemConfig {
    private String systemCode;
    private List<String> sharedSections;
}
