package com.taskflow.application.usecases;

import com.taskflow.application.dtos.TaskResponseDto;
import com.taskflow.application.dtos.UpdateTaskStatusRequestDto;
import com.taskflow.domain.entities.Task;
import com.taskflow.domain.exceptions.TaskNotFoundException;
import com.taskflow.domain.exceptions.UnauthorizedException;
import com.taskflow.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UpdateTaskStatusUseCase {
    
    private final TaskRepository taskRepository;
    
    public UpdateTaskStatusUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public TaskResponseDto execute(Long taskId, UpdateTaskStatusRequestDto request, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        
        // Verify task belongs to user
        if (!task.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this task");
        }
        
        // Update status and timestamp
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        
        return new TaskResponseDto(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.getPriority(),
            savedTask.getStatus(),
            savedTask.getUser().getId(),
            savedTask.getCreatedAt(),
            savedTask.getUpdatedAt()
        );
    }
}