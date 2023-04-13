package com.github.sibmaks.local_mocks.logger;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Slf4j
public class ApiLoggerFilter extends HttpFilter {
    private static final Set<String> FULL_PRINT_CONTENT_TYPE = Set.of(
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    );

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var rqUID = UUID.randomUUID().toString();
        var method = request.getMethod();
        var requestURI = request.getRequestURI();

        var requestWrapper = new ContentCachingRequestWrapper(request);
        var responseWrapper = new ContentCachingResponseWrapper(response);

        var startTime = System.currentTimeMillis();
        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            var timing = System.currentTimeMillis() - startTime;
            var status = responseWrapper.getStatus();
            var rqType = requestWrapper.getContentType();
            var rqBody = getRqBody(requestWrapper);
            var rsType = responseWrapper.getContentType();
            var rsBody = getRsBody(responseWrapper);
            responseWrapper.copyBodyToResponse();
            log.info("""
                    [INCOMING]
                    RqUID: '{}'
                    Method: '{}'
                    Uri: '{}'
                    Timing: '{} ms'
                    Status: '{}'
                    Rq-Content-Type: '{}'
                    Rq: '{}'
                    Rs-Content-Type: '{}'
                    Rs: '{}'""", rqUID, method, requestURI, timing, status, rqType, rqBody, rsType, rsBody);
        }
    }

    private String getRqBody(ContentCachingRequestWrapper request) {
        var contentType = request.getContentType();
        if (contentType != null && FULL_PRINT_CONTENT_TYPE.contains(contentType)) {
            byte[] contentAsByteArray = request.getContentAsByteArray();
            return new String(contentAsByteArray, StandardCharsets.UTF_8);
        }
        return "%d bytes".formatted(request.getContentLength());
    }

    private String getRsBody(ContentCachingResponseWrapper response) {
        var contentType = response.getContentType();
        if (contentType != null && FULL_PRINT_CONTENT_TYPE.contains(contentType)) {
            byte[] contentAsByteArray = response.getContentAsByteArray();
            return new String(contentAsByteArray, StandardCharsets.UTF_8);
        }
        return "%d bytes".formatted(response.getContentSize());
    }
}
