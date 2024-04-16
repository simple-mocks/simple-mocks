package com.github.simple_mocks.common.api.rs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.simple_mocks.common.api.dto.ErrorDto;
import lombok.*;

import java.io.Serializable;

/**
 * Standard response type for all APIs
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardRs<T extends Serializable> implements Serializable {
    private final boolean success;
    private final T body;
    private final ErrorDto error;

    public StandardRs() {
        this(true, null, null);
    }

    public StandardRs(boolean success) {
        this(success, null, null);
    }

    public StandardRs(boolean success, T body) {
        this(success, body, null);
    }

    public StandardRs(boolean success, ErrorDto error) {
        this(success, null, error);
    }
}
