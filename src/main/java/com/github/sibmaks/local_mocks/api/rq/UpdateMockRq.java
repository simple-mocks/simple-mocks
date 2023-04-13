package com.github.sibmaks.local_mocks.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMockRq implements Serializable {
    private long mockId;
    private String method;
    private String pathRegex;
    private String type;
    private Map<String, String> meta;
    private String content;
}
