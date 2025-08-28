package com.kiendh.springsecurity.service.schedule;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.kiendh.springsecurity.dto.enums.ScheduleType;
import com.kiendh.springsecurity.dto.request.TaskDefinition;
import com.kiendh.springsecurity.dto.response.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private final TaskStrategyFactory strategyFactory;

    // Map lưu trữ jobId tránh trùng lặp job
    Map<ScheduleType, ScheduledFuture<?>> jobsMap = new ConcurrentHashMap<>();

    // Enhanced version với metadata
    private final Map<ScheduleType, TaskMetadata> taskMetadata = new ConcurrentHashMap<>();

    public boolean scheduleATask(String cronExpression, TaskDefinition taskDefinition) {
        try {
            ScheduleType actionType = taskDefinition.getScheduleType();
            TaskStrategy strategy = strategyFactory.getStrategy(actionType);

            if (strategy == null) {
                System.out.println("Không tìm thấy loại lên lịch nào với loại: " + actionType);
                return false;
            }

            // Graceful shutdown của task cũ
            if (jobsMap.containsKey(actionType)) {
                System.out.println("Replacing existing task for type: " + actionType);
                removeScheduledTask(actionType);
            }

            strategy.setTaskDefinition(taskDefinition);

            // Store metadata trước khi schedule
            TaskMetadata metadata = new TaskMetadata(cronExpression, Instant.now(), 0);
            taskMetadata.put(actionType, metadata);

            // Wrap strategy với execution tracking
            Runnable wrappedStrategy = () -> {
                try {
                    strategy.run();
                    TaskMetadata runtimeMetadata = taskMetadata.get(actionType);
                    if (runtimeMetadata != null) {
                        runtimeMetadata.trackingSchedule();
                    }
                } catch (Exception e) {
                    System.out.println("Task execution failed for " + actionType + ": " + e.getMessage());
                }
            };

            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(wrappedStrategy,
                    new CronTrigger(cronExpression, TimeZone.getDefault()));

            jobsMap.put(actionType, scheduledTask);

            System.out.println("Scheduled task for type: " + actionType + " with cron: " + cronExpression);
            return true;

        } catch (Exception e) {
            System.out.println("Failed to schedule task: " + taskDefinition + ", " + e);
            return false;
        }
    }

    public void removeScheduledTask(ScheduleType actionType) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(actionType);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(actionType);
            taskMetadata.remove(actionType); // Cleanup metadata
        }
    }

    public Map<ScheduleType, TaskStatus> getActiveTasksStatus() {
        return jobsMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            ScheduleType type = entry.getKey();
                            ScheduledFuture<?> future = entry.getValue();
                            TaskMetadata metadata = taskMetadata.get(type);

                            if (metadata == null) {
                                metadata = new TaskMetadata("unknown", Instant.now(), 0);
                            }

                            return TaskStatus.builder()
                                    .scheduleType(type)
                                    .active(!future.isCancelled())
                                    .running(!future.isDone())
                                    .cronExpression(metadata.getCronExpression())
                                    .cronDescription(getCronDescription(metadata.getCronExpression()))
                                    .createdAt(metadata.getCreatedAt())
                                    .lastExecuted(metadata.getLastExecuted())
                                    .executionCount(metadata.getExecutionCount())
                                    .build();
                        }));
    }

    // Utility methods
    public int getActiveTaskCount() {
        return (int) jobsMap.values().stream()
                .filter(future -> !future.isCancelled())
                .count();
    }

    public boolean isTaskActive(ScheduleType scheduleType) {
        ScheduledFuture<?> future = jobsMap.get(scheduleType);
        return future != null && !future.isCancelled();
    }

    public String getTaskCronExpression(ScheduleType scheduleType) {
        TaskMetadata metadata = taskMetadata.get(scheduleType);
        return metadata != null ? metadata.getCronExpression() : null;
    }

    public long getTaskExecutionCount(ScheduleType scheduleType) {
        TaskMetadata metadata = taskMetadata.get(scheduleType);
        return metadata != null ? metadata.getExecutionCount() : 0;
    }

    private String getCronDescription(String cronExpression) {
        boolean isValidExpression = CronExpression.isValidExpression(cronExpression);

        if (!isValidExpression) {
            return "Invalid cron expression";
        }

        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING));
        Cron cron = parser.parse(cronExpression);
        CronDescriptor descriptor = CronDescriptor.instance(Locale.ENGLISH);
        return descriptor.describe(cron);
    }
}