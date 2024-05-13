package com.github.simple_mocks.local_mocks.service.handler;

import com.github.simple_mocks.local_mocks.entity.HttpMockEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestHandler {

    String getType();

    void handle(String path, HttpMockEntity httpMockEntity, HttpServletRequest rq, HttpServletResponse rs);
}