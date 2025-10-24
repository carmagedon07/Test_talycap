package com.taskflow.application.usecases;

import com.taskflow.domain.entities.Task;
import com.taskflow.domain.exceptions.TaskNotFoundException;
import com.taskflow.domain.exceptions.UnauthorizedException;
import com.taskflow.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskUseCase {
    
    private final TaskRepository taskRepository;
    
    public DeleteTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public void execute(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        
        // Verify task belongs to user
        if (!task.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this task");
        }
        
        taskRepository.deleteById(taskId);
    }
}