package com.kiendh.springsecurity.controller;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import com.kiendh.springsecurity.dto.request.TaskDefinition;
import com.kiendh.springsecurity.service.schedule.TaskSchedulingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class JobSchedulingController {

    private final TaskSchedulingService taskSchedulingService;


    @PostMapping("/taskdef")
    public ResponseEntity<String> scheduleATask(@RequestBody @Valid TaskDefinition taskDefinition) {

        boolean isValidExpression = CronExpression.isValidExpression(taskDefinition.getCronExpression());

        if (!isValidExpression) {
            return ResponseEntity.badRequest().body("Invalid cron expression");
        }

        boolean isScheduled = taskSchedulingService.scheduleATask(
                taskDefinition.getCronExpression(), taskDefinition);
        if (isScheduled) {
            return ResponseEntity.ok("Task scheduled successfully with scheduleType "
                    + taskDefinition.getScheduleType());
        } else {
            return ResponseEntity.badRequest()
                    .body("Failed to schedule task with scheduleType " + taskDefinition.getScheduleType());
        }
    }

    @GetMapping("/remove/{scheduleType}")
    public ResponseEntity<String> removeJob(@PathVariable ScheduleType scheduleType) {
        taskSchedulingService.removeScheduledTask(scheduleType);
        return ResponseEntity.ok("Task with scheduleType " + scheduleType + " removed successfully");
    }
}