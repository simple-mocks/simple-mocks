package com.github.sibmaks.storage_service.local.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.error_service.exception.ServiceException;
import com.github.sibmaks.storage_service.api.Content;
import com.github.sibmaks.storage_service.StorageService;
import com.github.sibmaks.storage_service.api.StorageErrors;
import com.github.sibmaks.storage_service.local.conf.LocalStorageServiceEnabled;
import com.github.sibmaks.storage_service.local.dto.LocalContent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Service
@ConditionalOnBean(LocalStorageServiceEnabled.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocalStorageService implements StorageService {
    @Value("${app.local.storage.folder}")
    private String folder;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setUp() {
        var file = new File(folder);
        if(file.exists()) {
            if(!file.isDirectory()) {
                throw new IllegalArgumentException("Path: %s exists and is not directory".formatted(folder));
            }
        } else {
            if (!file.mkdirs()) {
                throw new IllegalArgumentException("Can't create dirs: %s".formatted(folder));
            }
        }
    }

    @Override
    public String getStorageCode() {
        return "LOCAL";
    }

    @Override
    public Content get(String id) {
        var path = getPath(id);
        try (var channel = FileChannel.open(path, StandardOpenOption.READ)) {
            return LocalContent.read(objectMapper, channel);
        } catch (NoSuchFileException e) {
            throw new ServiceException(404, StorageErrors.NOT_FOUND, "File not found", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String create(Map<String, String> meta, byte[] content) {
        var id = UUID.randomUUID().toString();

        var now = ZonedDateTime.now();

        var localContent = LocalContent.builder()
                .id(id)
                .meta(meta == null ? Collections.emptyMap() : meta)
                .content(content)
                .createdAt(now)
                .modifiedAt(now)
                .build();

        var path = getPath(id);
        try (var channel = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            localContent.write(objectMapper, channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    private Path getPath(String id) {
        return Path.of(folder, "%s.data".formatted(id));
    }

}
