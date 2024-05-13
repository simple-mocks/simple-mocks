package com.github.simple_mocks.common.api.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.simple_mocks.common.api.dto.ErrorDto;
import lombok.Getter;

import java.io.Serializable;

/**
 * Standard response type for APIs without body
 *
 * @author sibmaks
 * @since 0.0.4
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardEmptyRs implements Serializable {
    private final boolean success;
    private final ErrorDto error;

    public StandardEmptyRs() {
        this.success = true;
        this.error = null;
    }

    public StandardEmptyRs(ErrorDto error) {
        this.success = false;
        this.error = error;
    }

}
