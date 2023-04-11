package com.github.sibmaks.storage_service.local.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibmaks.storage_service.api.Content;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalContent implements Content {
    private static final Base64.Encoder B64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    private final String id;
    private final Map<String, String> meta;
    private final byte[] content;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime modifiedAt;

    @Override
    public Map<String, String> getMeta() {
        return Collections.unmodifiableMap(meta);
    }

    @Override
    public byte[] getContent() {
        return Arrays.copyOf(content, content.length);
    }

    public static LocalContent read(ObjectMapper objectMapper, FileChannel fileChannel) throws IOException {
        try(var reader = new BufferedReader(Channels.newReader(fileChannel, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            SerializedContent serializedContent = objectMapper.readValue(builder.toString(), SerializedContent.class);

            String content64 = serializedContent.getContent();
            byte[] content = B64_DECODER.decode(content64);

            String id = serializedContent.getId();
            Map<String, String> meta = serializedContent.getMeta();
            ZonedDateTime createdAt = serializedContent.getCreatedAt();
            ZonedDateTime modifiedAt = serializedContent.getModifiedAt();

            return LocalContent.builder()
                    .id(id)
                    .meta(meta == null ? null : Collections.unmodifiableMap(meta))
                    .content(content)
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .build();
        }
    }

    public void write(ObjectMapper objectMapper, FileChannel fileChannel) throws IOException {
        try(var writer = new BufferedWriter(Channels.newWriter(fileChannel, StandardCharsets.UTF_8))) {
            String content64 = B64_ENCODER.encodeToString(content);

            SerializedContent serializedContent = SerializedContent.builder()
                    .id(id)
                    .meta(meta)
                    .content(content64)
                    .createdAt(createdAt)
                    .modifiedAt(modifiedAt)
                    .build();

            objectMapper.writeValue(writer, serializedContent);
        }
    }

}
