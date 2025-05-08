package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.service.IdempotentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IdempotentServiceImpl implements IdempotentService {

    private final StringRedisTemplate redisTemplate;
    private static final int TTL = 5 * 60;

    @Override
    public boolean markExecuted(String key) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(key, "executed", TTL, TimeUnit.SECONDS)
        );
    }
}
