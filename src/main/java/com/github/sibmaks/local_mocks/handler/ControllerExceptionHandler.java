package com.github.sibmaks.local_mocks.handler;

import com.github.sibmaks.error_service.ErrorService;
import com.github.sibmaks.error_service.exception.ServiceException;
import com.github.sibmaks.storage_service.local.api.rs.ErrorRs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${app.error.system_code}")
    private String systemCode;
    @Value("${app.error.default_code}")
    private String defaultCode;

    private final ErrorService errorService;

    @ExceptionHandler(ServiceException.class)
    public @ResponseBody ErrorRs handleServiceException(ServiceException e, HttpServletResponse response) {
        log.error("Service exception happened", e);
        var serviceError = e.getServiceError();
        var systemCode = serviceError.getSystemCode();
        var errorCode = serviceError.getErrorCode();
        var description = errorService.getDescription(systemCode, errorCode);
        response.setStatus(description.getStatusCode());
        return ErrorRs.builder()
                .system(description.getSystemCode())
                .code(description.getCode())
                .title(description.getTitle())
                .message(description.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorRs handleException(Exception e, HttpServletResponse response) {
        log.error("Exception happened", e);
        var description = errorService.getDescription(systemCode, defaultCode);
        response.setStatus(description.getStatusCode());
        return ErrorRs.builder()
                .system(description.getSystemCode())
                .code(description.getCode())
                .title(description.getTitle())
                .message(description.getMessage())
                .build();
    }

}
