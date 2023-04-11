package com.github.sibmaks.local_mocks.service.handler.impl.js.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@AllArgsConstructor
public class JsResponse {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpServletResponse rs;

    @HostAccess.Export
    public void status(int status) {
        rs.setStatus(status);
    }

    @HostAccess.Export
    public void header(String key, String value) {
        rs.setHeader(key, value);
    }

    @HostAccess.Export
    public void plain(String body) {
        ServletOutputStream outputStream;
        try {
            outputStream = rs.getOutputStream();
            outputStream.write(body.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @HostAccess.Export
    public void plain(int status, String contentType, String body) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        plain(body);
    }

    @HostAccess.Export
    public void bytes(byte[] bytes) {
        ServletOutputStream outputStream;
        try {
            outputStream = rs.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @HostAccess.Export
    public void bytes(int status, String contentType, byte[] bytes) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        bytes(bytes);
    }

    @HostAccess.Export
    public void json(Object json) {
        ServletOutputStream outputStream;
        try {
            outputStream = rs.getOutputStream();
            OBJECT_MAPPER.writeValue(outputStream, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @HostAccess.Export
    public void json(int status, String contentType, Object json) {
        status(status);
        header(HttpHeaders.CONTENT_TYPE, contentType);
        json(json);
    }

}
