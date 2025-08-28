package com.kiendh.springsecurity.dto.response;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {
    private ScheduleType scheduleType;
    private boolean active; // Task chưa bị cancel
    private boolean running; // Task chưa done
    private String cronExpression; // Cron hiện tại
    private String cronDescription; // Mô tả cron
    private Instant createdAt; // Thời gian tạo
    private Instant lastExecuted; // Lần chạy cuối
    private long executionCount; // Số lần đã chạy
    private String status; // "ACTIVE", "CANCELLED", "COMPLETED"

    // Simple constructor cho method hiện tại
    public TaskStatus(boolean active, boolean running) {
        this.active = active;
        this.running = running;
        this.status = active ? (running ? "ACTIVE" : "WAITING") : "CANCELLED";
    }
}
