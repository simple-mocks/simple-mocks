package com.github.simple_mocks.local_mocks.controller;

import com.github.simple_mocks.local_mocks.api.rq.CreateServiceRq;
import com.github.simple_mocks.local_mocks.api.rq.GetServiceRq;
import com.github.simple_mocks.local_mocks.api.rs.CreateServiceRs;
import com.github.simple_mocks.local_mocks.api.rs.GetServiceRs;
import com.github.simple_mocks.local_mocks.api.rs.GetServicesRs;
import com.github.simple_mocks.local_mocks.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.service.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiServiceController {
    private final MockService mockService;

    @Autowired
    public ApiServiceController(MockService mockService) {
        this.mockService = mockService;
    }

    @PostMapping("/create")
    public CreateServiceRs create(@RequestBody CreateServiceRq rq) {
        var code = rq.getCode();
        var serviceMockEntity = mockService.createService(code);
        return new CreateServiceRs(serviceMockEntity.getId());
    }

    @PostMapping("/get")
    public GetServiceRs get(@RequestBody GetServiceRq rq) {
        var serviceId = rq.getServiceId();
        var service = mockService.getService(serviceId);
        return new GetServiceRs(service);
    }

    @GetMapping("/getAll")
    public GetServicesRs getAll() {
        var services = mockService.getAllServices();
        return new GetServicesRs(services);
    }

}
