package com.github.simple_mocks.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HTTP_MOCK")
public class HttpMockEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "path_regex", nullable = false)
    private String pathRegex;
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "storage_type", nullable = false)
    private String storageType;
    @Column(name = "storage_id", nullable = false)
    private String storageId;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
