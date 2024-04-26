package com.github.simple_mocks.app.repository;

import com.github.simple_mocks.app.entity.HttpMockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface HttpMockEntityRepository extends JpaRepository<HttpMockEntity, Long> {

    List<HttpMockEntity> findAllByMethodAndServiceCode(String method, String serviceCode);

    List<HttpMockEntity> findAllByServiceId(long serviceId);

}
