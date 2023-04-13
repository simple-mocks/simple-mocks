package com.github.sibmaks.local_mocks.controller;

import com.github.sibmaks.local_mocks.api.rq.CreateMockRq;
import com.github.sibmaks.local_mocks.api.rq.GetMockRq;
import com.github.sibmaks.local_mocks.api.rq.UpdateMockRq;
import com.github.sibmaks.local_mocks.api.rs.CreateMockRs;
import com.github.sibmaks.local_mocks.api.rs.GetMockRs;
import com.github.sibmaks.local_mocks.api.rs.UpdateMockRs;
import com.github.sibmaks.local_mocks.service.MockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.mock.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiMockController {
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    private final MockService mockService;

    @PostMapping("/create")
    public CreateMockRs create(@RequestBody CreateMockRq rq) {
        var content = rq.getContent();
        var httpMockEntity = mockService.create(rq.getServiceId(),
                rq.getMethod(),
                rq.getPathRegex(),
                rq.getType(),
                rq.getMeta(),
                B64_DECODER.decode(content));
        return new CreateMockRs(httpMockEntity.getId());
    }

    @PostMapping("/update")
    public UpdateMockRs update(@RequestBody UpdateMockRq rq) {
        var content = rq.getContent();
        var httpMockEntity = mockService.update(rq.getMockId(),
                rq.getMethod(),
                rq.getPathRegex(),
                rq.getType(),
                rq.getMeta(),
                B64_DECODER.decode(content));
        return new UpdateMockRs(httpMockEntity.getId());
    }

    @PostMapping("/get")
    public GetMockRs get(@RequestBody GetMockRq rq) {
        var mockDto = mockService.get(rq.getMockId());
        return new GetMockRs(mockDto);
    }

}
