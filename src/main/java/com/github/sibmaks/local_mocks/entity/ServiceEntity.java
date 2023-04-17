package com.github.sibmaks.local_mocks.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SERVICE")
public class ServiceEntity {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
