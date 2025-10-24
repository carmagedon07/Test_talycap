package com.taskflow.infrastructure.repositories;

import com.taskflow.domain.entities.Task;
import com.taskflow.domain.enums.TaskStatus;
import com.taskflow.domain.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskRepositoryAdapter implements TaskRepository {
    
    private final JpaTaskRepository jpaTaskRepository;
    
    public TaskRepositoryAdapter(JpaTaskRepository jpaTaskRepository) {
        this.jpaTaskRepository = jpaTaskRepository;
    }
    
    @Override
    public Task save(Task task) {
        return jpaTaskRepository.save(task);
    }
    
    @Override
    public Optional<Task> findById(Long id) {
        return jpaTaskRepository.findById(id);
    }
    
    @Override
    public List<Task> findByUserId(Long userId) {
        return jpaTaskRepository.findByUserId(userId);
    }
    
    @Override
    public List<Task> findByUserIdAndStatus(Long userId, TaskStatus status) {
        return jpaTaskRepository.findByUserIdAndStatus(userId, status);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaTaskRepository.deleteById(id);
    }
}