package com.taskflow.application.usecases;

import com.taskflow.application.dtos.TaskResponseDto;
import com.taskflow.domain.entities.Task;
import com.taskflow.domain.enums.TaskStatus;
import com.taskflow.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetTasksByStatusUseCase {
    
    private final TaskRepository taskRepository;
    
    public GetTasksByStatusUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public List<TaskResponseDto> execute(Long userId, TaskStatus status) {
        List<Task> tasks = taskRepository.findByUserIdAndStatus(userId, status);
        
        return tasks.stream()
            .map(task -> new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getUser().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
            ))
            .collect(Collectors.toList());
    }
}