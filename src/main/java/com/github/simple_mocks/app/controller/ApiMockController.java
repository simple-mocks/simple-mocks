package com.github.simple_mocks.app.controller;

import com.github.simple_mocks.app.api.rq.CreateMockRq;
import com.github.simple_mocks.app.api.rq.GetMockRq;
import com.github.simple_mocks.app.api.rq.UpdateMockRq;
import com.github.simple_mocks.app.api.rs.CreateMockRs;
import com.github.simple_mocks.app.api.rs.GetMockRs;
import com.github.simple_mocks.app.api.rs.UpdateMockRs;
import com.github.simple_mocks.app.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

/**
 * CRUD controller for mocks
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping(value = "${app.uri.api.mock.path}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiMockController {
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    private final MockService mockService;

    /**
     * Construct api mock controller
     *
     * @param mockService mock service
     */
    @Autowired
    public ApiMockController(MockService mockService) {
        this.mockService = mockService;
    }

    /**
     * Create a new mock.
     *
     * @param rq mock creation request
     * @return mock creation result
     */
    @PostMapping("/create")
    public CreateMockRs create(@RequestBody CreateMockRq rq) {
        var content = rq.getContent();
        var httpMockEntity = mockService.create(
                rq.getServiceId(),
                rq.getMethod(),
                rq.getPathRegex(),
                rq.getType(),
                rq.getMeta(),
                B64_DECODER.decode(content)
        );
        return new CreateMockRs(httpMockEntity.getId());
    }

    /**
     * Update existing mock.
     *
     * @param rq mock update request
     * @return mock update result
     */
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

    /**
     * Get mock information request
     *
     * @param rq mock identification rq
     * @return mock information
     */
    @PostMapping("/get")
    public GetMockRs get(@RequestBody GetMockRq rq) {
        var mockDto = mockService.get(rq.getMockId());
        return new GetMockRs(mockDto);
    }

}
