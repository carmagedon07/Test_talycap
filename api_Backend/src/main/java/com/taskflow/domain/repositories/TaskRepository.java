package com.taskflow.domain.repositories;

import com.taskflow.domain.entities.Task;
import com.taskflow.domain.enums.TaskStatus;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
    void deleteById(Long id);
}