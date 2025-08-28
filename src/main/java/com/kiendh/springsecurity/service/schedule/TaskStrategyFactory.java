package com.kiendh.springsecurity.service.schedule;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskStrategyFactory {

    private final List<TaskStrategy> strategies;
    private final Map<ScheduleType, TaskStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void initialize() {
        for (TaskStrategy strategy : strategies) {
            strategyMap.put(strategy.getActionType(), strategy);
        }
        log.info("Initialized {} task strategies: {}", strategyMap.size(), strategyMap.keySet());
    }

    public TaskStrategy getStrategy(ScheduleType actionType) {
        return strategyMap.getOrDefault(actionType, strategyMap.get(ScheduleType.DEFAULT));
    }
}