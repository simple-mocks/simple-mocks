package com.github.sibmaks.local_mocks.repository;

import com.github.sibmaks.local_mocks.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
}
