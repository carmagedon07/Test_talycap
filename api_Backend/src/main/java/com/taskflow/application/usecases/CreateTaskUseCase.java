package com.taskflow.application.usecases;

import com.taskflow.application.dtos.CreateTaskRequestDto;
import com.taskflow.application.dtos.TaskResponseDto;
import com.taskflow.domain.entities.Task;
import com.taskflow.domain.entities.User;
import com.taskflow.domain.enums.TaskStatus;
import com.taskflow.domain.exceptions.UserNotFoundException;
import com.taskflow.domain.repositories.TaskRepository;
import com.taskflow.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateTaskUseCase {
    
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    
    public CreateTaskUseCase(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    
    public TaskResponseDto execute(CreateTaskRequestDto request, Long userId) {
        // Verify user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        // Create new task
        Task task = new Task(
            request.getTitle(),
            request.getDescription(),
            request.getPriority(),
            user
        );
        
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