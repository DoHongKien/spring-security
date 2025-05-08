package com.kiendh.springsecurity.config;

import com.kiendh.springsecurity.infrastructure.interceptors.LoggingRequestResponseClientInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .readTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .interceptors(List.of(new LoggingRequestResponseClientInterceptor()))
                .build();
    }

    @Bean
    public RestTemplate telegramRestTemplate(RestTemplateBuilder builder) {
        return builder
                .readTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60)).build();
    }
}
