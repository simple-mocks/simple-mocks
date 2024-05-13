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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardRs<T extends Serializable> extends StandardEmptyRs {
    private final T body;

    public StandardRs() {
        this.body = null;
    }

    public StandardRs(T body) {
        this.body = body;
    }

    public StandardRs(ErrorDto error) {
        super(error);
        this.body = null;
    }
}
