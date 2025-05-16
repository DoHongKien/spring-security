package com.kiendh.springsecurity.service.schedule;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import com.kiendh.springsecurity.dto.request.TaskDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;
    private final TaskStrategyFactory strategyFactory;

    // Map lưu trữ jobId tránh trùng lặp job
    Map<ScheduleType, String> actionTypeToJobIdMap = new HashMap<>();
    Map<ScheduleType, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public boolean scheduleATask(String cronExpression, TaskDefinition taskDefinition) {
        ScheduleType actionType = taskDefinition.getScheduleType();
        TaskStrategy strategy = strategyFactory.getStrategy(actionType);
        if (strategy == null) {
            System.out.println("Không tìm thấy loại lên lịch nào với loại: " + actionType);
            return false;
        }
        strategy.setTaskDefinition(taskDefinition);

        // Kiểm tra nếu actionType đã được lên lịch thì hủy trình lên lịch cũ
        if (actionTypeToJobIdMap.containsKey(actionType)) {
            removeScheduledTask(actionType);
        }

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(strategy,
                new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(actionType, scheduledTask);
        actionTypeToJobIdMap.put(actionType, "scheduled");
        return true;
    }

    public void removeScheduledTask(ScheduleType actionType) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(actionType);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(actionType);
            actionTypeToJobIdMap.entrySet().removeIf(entry -> entry.getKey().equals(actionType));
        }
    }
}