package com.github.sibmaks.local_mocks.repository;

import com.github.sibmaks.local_mocks.entity.HttpMockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sibmaks
 * @since 2023-04-11
 */
public interface HttpMockEntityRepository extends JpaRepository<HttpMockEntity, Long> {

    List<HttpMockEntity> findAllByMethodAndServiceCode(String method, String serviceCode);

}
