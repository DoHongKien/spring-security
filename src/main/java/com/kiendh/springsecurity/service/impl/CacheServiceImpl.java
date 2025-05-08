package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Cacheable(cacheManager = "normalCache", key = "#message", cacheNames = "hello")
    public String conversation(String message) {
        return "Hello " + message;
    }
}
