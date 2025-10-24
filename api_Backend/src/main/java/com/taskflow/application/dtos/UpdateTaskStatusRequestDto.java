package com.taskflow.application.dtos;

import com.taskflow.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusRequestDto {
    
    @NotNull(message = "Status is required")
    private TaskStatus status;
    
    public UpdateTaskStatusRequestDto() {}
    
    public UpdateTaskStatusRequestDto(TaskStatus status) {
        this.status = status;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}