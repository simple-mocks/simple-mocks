package com.github.simple_mocks.app.service.async;

import com.github.simple_mocks.app.entity.HttpMockEntity;
import com.github.simple_mocks.app.repository.HttpMockEntityRepository;
import com.github.simple_mocks.async.api.entity.AsyncTask;
import com.github.simple_mocks.async.api.rs.AsyncTaskProcessingResult;
import com.github.simple_mocks.async.api.rs.AsyncTaskProcessingResultBuilder;
import com.github.simple_mocks.async.api.service.AsyncTaskProcessor;
import com.github.simple_mocks.async.api.service.AsyncTaskProcessorMeta;
import com.github.simple_mocks.error_service.exception.ServiceException;
import com.github.simple_mocks.storage.api.StorageErrors;
import com.github.simple_mocks.storage.api.StorageService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.3
 */
@Slf4j
@Component
@AsyncTaskProcessorMeta(
        taskType = ContentRemoveTaskProcessor.TASK_TYPE,
        taskVersions = "1.0.0"
)
public class ContentRemoveTaskProcessor implements AsyncTaskProcessor {
    public static final String TASK_TYPE = "remove-content-after-update";

    private final HttpMockEntityRepository httpMockEntityRepository;
    private final StorageService storageService;

    /**
     * Construct clean up task processor
     *
     * @param httpMockEntityRepository instance of {@link HttpMockEntityRepository}
     * @param storageService           instance of storage service
     */
    public ContentRemoveTaskProcessor(HttpMockEntityRepository httpMockEntityRepository,
                                      StorageService storageService) {
        this.httpMockEntityRepository = httpMockEntityRepository;
        this.storageService = storageService;
    }

    @Nonnull
    @Override
    public AsyncTaskProcessingResult process(@Nonnull AsyncTask task) {
        var parameters = task.parameters();
        var mockId = Long.valueOf(parameters.get("mockId"));
        var storageId = parameters.get("storageId");

        var currentStorageId = httpMockEntityRepository.findById(mockId)
                .map(HttpMockEntity::getStorageId)
                .orElse(null);

        if (Objects.equals(storageId, currentStorageId)) {
            log.warn("Storage '{}' can't be removed because mock linked to id", storageId);
            return AsyncTaskProcessingResultBuilder.createFinishResult();
        }

        try {
            storageService.delete(storageId);
            return AsyncTaskProcessingResultBuilder.createFinishResult();
        } catch (ServiceException e) {
            log.error("Content clean up failed", e);
            if (StorageErrors.NOT_FOUND.equals(e.getServiceError())) {
                log.warn("Content already removed");
                return AsyncTaskProcessingResultBuilder.createFinishResult();
            }
        }

        return AsyncTaskProcessingResultBuilder.createRetryResult(
                ZonedDateTime.now()
                        .plusMinutes(1)
        );
    }
}
