package com.github.simple_mocks.app.controller;

import com.github.simple_mocks.app.service.RequestDispatcher;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("${app.uri.mock.path}")
public class MockController {
    @Value("${app.uri.mock.path}")
    private String uriPath;

    private final RequestDispatcher requestDispatcher;

    @Autowired
    public MockController(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    @PostConstruct
    public void setUp() {
        if (!uriPath.endsWith("/")) {
            uriPath += "/";
        }
    }

    @RequestMapping("/{serviceCode}/**")
    public void call(@PathVariable("serviceCode") String serviceCode,
                     HttpServletRequest rq,
                     HttpServletResponse rs) {
        var method = rq.getMethod();
        var path = getPath(serviceCode, rq);
        requestDispatcher.dispatch(method, serviceCode, path, rq, rs);
    }

    private String getPath(String serviceCode, HttpServletRequest rq) {
        var prefix = uriPath + serviceCode;
        var servletPath = rq.getServletPath();
        return servletPath.substring(prefix.length());
    }

}
