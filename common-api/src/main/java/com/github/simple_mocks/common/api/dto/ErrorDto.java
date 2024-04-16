package com.github.simple_mocks.common.api.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Builder
public record ErrorDto(String system, String code, String title, String message) implements Serializable {
}
