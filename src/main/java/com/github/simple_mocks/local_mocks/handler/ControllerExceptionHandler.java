package com.github.simple_mocks.local_mocks.handler;

import com.github.simple_mocks.common.api.dto.ErrorDto;
import com.github.simple_mocks.common.api.rs.StandardRs;
import com.github.simple_mocks.error_service.ErrorService;
import com.github.simple_mocks.error_service.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${app.error.system_code}")
    private String systemCode;
    @Value("${app.error.default_code}")
    private String defaultCode;

    private final ErrorService errorService;

    @Autowired
    public ControllerExceptionHandler(ErrorService errorService) {
        this.errorService = errorService;
    }

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody StandardRs<?> handleServiceException(ServiceException e, HttpServletResponse response) {
        log.error("Service exception happened", e);
        var status = e.getStatus();
        var serviceError = e.getServiceError();
        var systemCode = serviceError.getSystemCode();
        var errorCode = serviceError.getErrorCode();
        return handleException(status, systemCode, errorCode, response);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody StandardRs<?> handleException(Exception e, HttpServletResponse response) {
        log.error("Exception happened", e);
        return handleException(502, systemCode, defaultCode, response);
    }

    private StandardRs<? extends Serializable> handleException(int statusCode,
                                                               String systemCode,
                                                               String errorCode,
                                                               HttpServletResponse response) {
        try {
            var description = errorService.getDescription(systemCode, errorCode, Locale.ENGLISH);
            response.setStatus(statusCode);
            var errorDto = ErrorDto.builder()
                    .system(description.getSystemCode())
                    .code(description.getCode())
                    .title(description.getTitle())
                    .message(description.getMessage())
                    .build();
            return new StandardRs<>(false, errorDto);
        } catch (Throwable t) {
            log.error("Error get error description", t);
            return getDefaultErrorRs(response);
        }
    }

    private StandardRs<?> getDefaultErrorRs(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } catch (Throwable e) {
            log.error("Can't set response status", e);
        }
        var errorDto = ErrorDto.builder()
                .system(systemCode)
                .code(defaultCode)
                .build();
        return new StandardRs<>(false, errorDto);
    }

}
