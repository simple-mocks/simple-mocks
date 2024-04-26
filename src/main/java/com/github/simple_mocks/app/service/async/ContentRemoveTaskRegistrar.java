package com.github.simple_mocks.app.service.async;

import com.github.simple_mocks.app.api.MocksErrors;
import com.github.simple_mocks.async.api.rq.CreateAsyncTaskRq;
import com.github.simple_mocks.async.api.service.AsyncTaskService;
import com.github.simple_mocks.error_service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Service
public class ContentRemoveTaskRegistrar {
    private final AsyncTaskService asyncTaskService;

    /**
     * Construct content removal task registrar
     *
     * @param asyncTaskService instance of the async task service
     */
    public ContentRemoveTaskRegistrar(AsyncTaskService asyncTaskService) {
        this.asyncTaskService = asyncTaskService;
    }

    /**
     * Register a task for content removal
     *
     * @param mockId    mock id
     * @param storageId storage identifier
     */
    public void register(long mockId, String storageId) {
        var createTaskRq = CreateAsyncTaskRq.builder()
                .uid(UUID.randomUUID().toString())
                .type(ContentRemoveTaskProcessor.TASK_TYPE)
                .version("1.0.0")
                .scheduledStartTime(
                        ZonedDateTime.now()
                                .plusSeconds(10)
                )
                .parameters(Map.of(
                        "mockId", String.valueOf(mockId),
                        "storageId", storageId
                ))
                .build();
        if (!asyncTaskService.registerTask(createTaskRq)) {
            throw new ServiceException(MocksErrors.UNEXPECTED_ERROR, "Can't register clean up task");
        }
    }
}
