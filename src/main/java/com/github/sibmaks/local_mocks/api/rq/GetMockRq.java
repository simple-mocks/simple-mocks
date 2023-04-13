package com.github.sibmaks.local_mocks.api.rq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMockRq implements Serializable {
    private long mockId;
}
