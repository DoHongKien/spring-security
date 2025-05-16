package com.kiendh.springsecurity.service.schedule.handler;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import com.kiendh.springsecurity.dto.request.TaskDefinition;
import com.kiendh.springsecurity.service.schedule.TaskStrategy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
public class DefaultScheduleHandler implements TaskStrategy {

    private TaskDefinition taskDefinition;

    @Override
    public void run() {
        log.info("Default schedule handler with cron: {}", taskDefinition.getCronExpression());
    }

    @Override
    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
    }

    @Override
    public ScheduleType getActionType() {
        return ScheduleType.DEFAULT;
    }
}
