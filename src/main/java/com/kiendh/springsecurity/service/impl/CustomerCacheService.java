package com.kiendh.springsecurity.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kiendh.springsecurity.entity.Customer;
import com.kiendh.springsecurity.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerCacheService {

    private final CustomerService customerService;

    private static final Cache<String, Customer> customerCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(12)
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .build();

    public Customer getCustomerLocalCache(Long customerId) {
        try {
            return customerCache.getIfPresent(createKeyCache(customerId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createKeyCache(Long customerId) {
        return "customer::" + customerId;
    }
}
