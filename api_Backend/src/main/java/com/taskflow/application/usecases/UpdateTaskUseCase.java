package com.taskflow.application.usecases;

import com.taskflow.application.dtos.TaskResponseDto;
import com.taskflow.application.dtos.UpdateTaskRequestDto;
import com.taskflow.domain.entities.Task;
import com.taskflow.domain.exceptions.TaskNotFoundException;
import com.taskflow.domain.exceptions.UnauthorizedException;
import com.taskflow.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateTaskUseCase {
    
    private final TaskRepository taskRepository;
    
    public UpdateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public TaskResponseDto execute(Long taskId, UpdateTaskRequestDto request, Long userId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        // Verificar que la tarea pertenece al usuario
        if (!task.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to update this task");
        }
        
        // Actualizar los campos de la tarea
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        
        Task updatedTask = taskRepository.save(task);
        
        return new TaskResponseDto(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getPriority(),
            updatedTask.getStatus(),
            updatedTask.getUser().getId(),
            updatedTask.getCreatedAt(),
            updatedTask.getUpdatedAt()
        );
    }
}