package com.celery_equivalent.repository;


import com.celery_equivalent.entity.URLMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface URLMetadataRepository extends JpaRepository<URLMetadata, Long> {
}
