package com.celery_equivalent.service;

import com.celery_equivalent.entity.URLMetadata;
import com.celery_equivalent.repository.URLMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ScrapingService {

    private final URLMetadataRepository repository;
    private final StringRedisTemplate redisTemplate;

    @Async
    public void scrapeUrls(List<String> urls, String taskId) {
        for (String url : urls) {
            // Implement scraping logic here
            URLMetadata metadata = new URLMetadata();
            metadata.setUrl(url);
            metadata.setTitle("Sample Title");
            metadata.setDescription("Sample Description");
            metadata.setKeywords("Sample Keywords");
            repository.save(metadata);
        }
        redisTemplate.opsForValue().set(taskId, "completed");
    }
}