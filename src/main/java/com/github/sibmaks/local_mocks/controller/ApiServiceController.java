package com.github.sibmaks.local_mocks.controller;

import com.github.sibmaks.local_mocks.api.rq.CreateServiceRq;
import com.github.sibmaks.local_mocks.api.rq.GetServiceRq;
import com.github.sibmaks.local_mocks.api.rs.CreateServiceRs;
import com.github.sibmaks.local_mocks.api.rs.GetServiceRs;
import com.github.sibmaks.local_mocks.service.MockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.service.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiServiceController {
    private final MockService mockService;

    @PostMapping("/create")
    public CreateServiceRs create(@RequestBody CreateServiceRq rq) {
        var code = rq.getCode();
        var serviceMockEntity = mockService.createService(code);
        return new CreateServiceRs(serviceMockEntity.getId());
    }

    @PostMapping("/get")
    public GetServiceRs create(@RequestBody GetServiceRq rq) {
        var serviceId = rq.getServiceId();
        var service = mockService.getService(serviceId);
        return new GetServiceRs(service);
    }

}
