package com.github.sibmaks.local_mocks.service.handler.impl.js.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.graalvm.polyglot.HostAccess;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public class JsRequest {
    @HostAccess.Export
    public final String method;
    @HostAccess.Export
    public final String path;
    @HostAccess.Export
    public final Map<String, String> headers;
    private final CompletableFuture<byte[]> contentFuture;

    public JsRequest(String path, HttpServletRequest rq) {
        this.method = rq.getMethod();
        this.path = path;
        var headers = getHeaders(rq);
        this.headers = Collections.unmodifiableMap(headers);
        this.contentFuture = getContentFuture(rq);
    }

    private static CompletableFuture<byte[]> getContentFuture(HttpServletRequest rq) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var inputStream = rq.getInputStream();
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Map<String, String> getHeaders(HttpServletRequest rq) {
        var headers = new HashMap<String, String>();
        var headerNames = rq.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var headerKey = headerNames.nextElement();
            var headerValue = rq.getHeader(headerKey);
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    @HostAccess.Export
    public byte[] bytes() throws ExecutionException, InterruptedException {
        return contentFuture.get();
    }

    @HostAccess.Export
    public String text() throws ExecutionException, InterruptedException {
        return new String(contentFuture.get(), StandardCharsets.UTF_8);
    }

}
