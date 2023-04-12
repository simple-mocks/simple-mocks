package com.github.sibmaks.local_mocks.service.handler.impl.js.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Builder
@AllArgsConstructor
public class LocalMocksContext {
    @HostAccess.Export
    public final JsRequest request;
    @HostAccess.Export
    public final JsResponse response;
    @HostAccess.Export
    public final JsSessions sessions;
}
