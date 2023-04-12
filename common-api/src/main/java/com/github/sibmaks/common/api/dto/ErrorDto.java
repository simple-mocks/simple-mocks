package com.github.sibmaks.common.api.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-12
 */
@Builder
public record ErrorDto(String system, String code, String title, String message) implements Serializable {
}
