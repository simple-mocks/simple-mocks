package com.github.simple_mocks.local_mocks.service.handler.impl.js.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Builder
@AllArgsConstructor
public class SimleMocksContext {
    @HostAccess.Export
    public final JsRequest request;
    @HostAccess.Export
    public final JsResponse response;
    @HostAccess.Export
    public final JsSessions sessions;
}
