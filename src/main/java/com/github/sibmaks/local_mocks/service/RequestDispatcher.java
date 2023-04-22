package com.github.sibmaks.local_mocks.service;

import com.github.sibmaks.local_mocks.repository.HttpMockEntityRepository;
import com.github.sibmaks.local_mocks.service.handler.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestDispatcher {
    private final Map<String, RequestHandler> handlers;
    private final HttpMockEntityRepository httpMockEntityRepository;

    @Autowired
    public RequestDispatcher(List<RequestHandler> handlers,
                             HttpMockEntityRepository httpMockEntityRepository) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(RequestHandler::getType, it -> it));
        this.httpMockEntityRepository = httpMockEntityRepository;
    }

    public void dispatch(String method,
                         String serviceCode,
                         String path,
                         HttpServletRequest rq,
                         HttpServletResponse rs) {
        var httpMocks = httpMockEntityRepository.findAllByMethodAndServiceCode(method, serviceCode);
        for (var httpMock : httpMocks) {
            var pathRegex = httpMock.getPathRegex();
            if(path.matches(pathRegex)) {
                var type = httpMock.getType();
                var requestHandler = handlers.get(type);
                if(requestHandler == null) {
                    throw new IllegalStateException("Request handler with type %s not exists".formatted(type));
                }
                requestHandler.handle(path, httpMock, rq, rs);
                return;
            }
        }
        throw new IllegalArgumentException("Handler for %s %s %s not found".formatted(method, serviceCode, path));
    }

}
