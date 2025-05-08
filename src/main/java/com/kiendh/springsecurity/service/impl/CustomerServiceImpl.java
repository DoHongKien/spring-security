package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.dto.response.CustomerResponse;
import com.kiendh.springsecurity.repository.CustomerRepository;
import com.kiendh.springsecurity.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Cacheable(cacheManager = "vipCache", key = "#id", cacheNames = "customer")
    @Override
    public CustomerResponse getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::new)
                .orElse(null);
    }
}
