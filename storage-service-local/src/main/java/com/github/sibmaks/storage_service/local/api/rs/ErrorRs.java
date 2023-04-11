package com.github.sibmaks.storage_service.local.api.rs;

import lombok.*;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRs implements Serializable {
    private String system;
    private String code;
    private String title;
    private String message;
}
