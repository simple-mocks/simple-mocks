package com.github.sibmaks.storage_service.local.api.rs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateContentRs implements Serializable {
    private String id;
}
