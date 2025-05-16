package com.kiendh.springsecurity.service.schedule;

import com.kiendh.springsecurity.dto.enums.ScheduleType;
import com.kiendh.springsecurity.dto.request.TaskDefinition;

/**
 * Interface defining the strategy for executing different types of tasks.
 * Each task type will have its own implementation of this interface.
 */
public interface TaskStrategy extends Runnable {

    /**
     * Sets the task definition for the strategy.
     * 
     * @param taskDefinition The task definition containing configuration and data.
     */
    void setTaskDefinition(TaskDefinition taskDefinition);

    /**
     * Gets the action type this strategy handles.
     * 
     * @return The action type associated with this strategy.
     */
    ScheduleType getActionType();
}