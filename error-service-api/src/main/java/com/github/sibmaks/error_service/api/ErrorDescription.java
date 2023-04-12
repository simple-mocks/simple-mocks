package com.github.sibmaks.error_service.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Error description, contains required status code for http response, system code, code, user friendly title and message
 *
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDescription implements Serializable {
    private int statusCode;
    private String systemCode;
    private String code;
    private String title;
    private String message;
}
