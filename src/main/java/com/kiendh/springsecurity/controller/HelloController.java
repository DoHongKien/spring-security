package com.kiendh.springsecurity.controller;

import com.kiendh.springsecurity.dto.response.CustomerResponse;
import com.kiendh.springsecurity.service.CacheService;
import com.kiendh.springsecurity.service.CustomerService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final CacheService cacheService;
    private final CustomerService customerService;

    @GetMapping("hello")
    public String hello() {
    return cacheService.conversation("World");
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("hi")
    public String hi() {
        return "Hi World";
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("customer")
    public CustomerResponse getCustomer() {
        return customerService.getCustomer(1L);
    }
}
