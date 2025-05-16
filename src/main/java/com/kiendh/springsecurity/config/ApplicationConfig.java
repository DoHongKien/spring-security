package com.kiendh.springsecurity.config;

import com.kiendh.springsecurity.service.schedule.TaskStrategyFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final TaskStrategyFactory taskStrategyFactory;

    @PostConstruct
    public void init() {
        taskStrategyFactory.initialize();
    }
}