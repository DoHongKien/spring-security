package com.kiendh.springsecurity.dto.request;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDefinition {

    @NotNull(message = "Cron expression cannot be null")
    private String cronExpression;
    private ScheduleType scheduleType;
}
