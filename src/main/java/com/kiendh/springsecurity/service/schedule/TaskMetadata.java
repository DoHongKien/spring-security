package com.kiendh.springsecurity.service.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMetadata {
    private String cronExpression;
    private Instant createdAt;
    private AtomicLong executionCount;
    private volatile Instant lastExecuted;

    // Constructor cho việc khởi tạo đơn giản
    public TaskMetadata(String cronExpression, Instant createdAt, long initialCount) {
        this.cronExpression = cronExpression;
        this.createdAt = createdAt;
        this.executionCount = new AtomicLong(initialCount);
    }

    // Increment execution count (thread-safe)
    public void trackingSchedule() {
        this.lastExecuted = Instant.now();
        executionCount.incrementAndGet();
    }

    // Get execution count as long
    public long getExecutionCount() {
        return executionCount.get();
    }

    // Update cron expression
    public void updateCronExpression(String newCronExpression) {
        this.cronExpression = newCronExpression;
    }
}
