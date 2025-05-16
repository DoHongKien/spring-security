package com.kiendh.springsecurity.service.schedule;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskStrategyFactory {

    private final List<TaskStrategy> strategies;
    private final Map<ScheduleType, TaskStrategy> strategyMap = new HashMap<>();

    public void initialize() {
        for (TaskStrategy strategy : strategies) {
            strategyMap.put(strategy.getActionType(), strategy);
        }
    }

    public TaskStrategy getStrategy(ScheduleType actionType) {
        return strategyMap.getOrDefault(actionType, strategyMap.get(ScheduleType.DEFAULT));
    }
}