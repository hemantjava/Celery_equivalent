package com.celery_equivalent.controller;

import com.celery_equivalent.entity.URLMetadata;
import com.celery_equivalent.repository.URLMetadataRepository;
import com.celery_equivalent.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value ="/api")
@RequiredArgsConstructor
public class URLController {


    private final ScrapingService scrapingService;


    private final StringRedisTemplate redisTemplate;

    private final URLMetadataRepository  repository;


    @PostMapping(value = "/upload")
    public String uploadCSV(@RequestParam("file") MultipartFile file) {
        if (!file.getContentType().equals("text/csv")) {
            throw new RuntimeException("File must be a CSV");
        }

        List<String> urls;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            urls = br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file", e);
        }

        String taskId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(taskId, "pending");
        scrapingService.scrapeUrls(urls, taskId);
        return taskId;
    }

    @GetMapping("/status/{taskId}")
    public String checkStatus(@PathVariable String taskId) {
        String status = redisTemplate.opsForValue().get(taskId);
        if (status == null) {
            throw new RuntimeException("Task not found");
        }
        return status;
    }

    @GetMapping("/results/{taskId}")
    public List<URLMetadata> getResults(@PathVariable Long taskId) {
        //Need to check
        return List.of(repository.findById(taskId).get());
    }
}