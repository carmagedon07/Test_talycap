package com.taskflow.application.usecases;

import com.taskflow.application.dtos.UserProfileDto;
import com.taskflow.domain.entities.Task;
import com.taskflow.domain.entities.User;
import com.taskflow.domain.enums.TaskStatus;
import com.taskflow.domain.exceptions.UserNotFoundException;
import com.taskflow.domain.repositories.TaskRepository;
import com.taskflow.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserProfileUseCase {
    
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    
    public GetUserProfileUseCase(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }
    
    public UserProfileDto execute(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        List<Task> allTasks = taskRepository.findByUserId(userId);
        
        int totalTasks = allTasks.size();
        int pendingTasks = (int) allTasks.stream()
            .filter(task -> task.getStatus() == TaskStatus.POR_HACER)
            .count();
        int inProgressTasks = (int) allTasks.stream()
            .filter(task -> task.getStatus() == TaskStatus.EN_PROGRESO)
            .count();
        int completedTasks = (int) allTasks.stream()
            .filter(task -> task.getStatus() == TaskStatus.COMPLETADA)
            .count();
        
        return new UserProfileDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            totalTasks,
            pendingTasks,
            inProgressTasks,
            completedTasks
        );
    }
}