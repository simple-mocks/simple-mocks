package com.github.simple_mocks.app.repository;

import com.github.simple_mocks.app.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
}
