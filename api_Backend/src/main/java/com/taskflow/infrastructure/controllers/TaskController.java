package com.taskflow.infrastructure.controllers;

import com.taskflow.application.dtos.*;
import com.taskflow.application.usecases.*;
import com.taskflow.domain.enums.TaskStatus;
import com.taskflow.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final CreateTaskUseCase createTaskUseCase;
    private final GetUserTasksUseCase getUserTasksUseCase;
    private final UpdateTaskStatusUseCase updateTaskStatusUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final GetTasksByStatusUseCase getTasksByStatusUseCase;
    private final TokenService tokenService;
    
    public TaskController(CreateTaskUseCase createTaskUseCase,
                         GetUserTasksUseCase getUserTasksUseCase,
                         UpdateTaskStatusUseCase updateTaskStatusUseCase,
                         UpdateTaskUseCase updateTaskUseCase,
                         DeleteTaskUseCase deleteTaskUseCase,
                         GetTasksByStatusUseCase getTasksByStatusUseCase,
                         TokenService tokenService) {
        this.createTaskUseCase = createTaskUseCase;
        this.getUserTasksUseCase = getUserTasksUseCase;
        this.updateTaskStatusUseCase = updateTaskStatusUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.getTasksByStatusUseCase = getTasksByStatusUseCase;
        this.tokenService = tokenService;
    }
    
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody CreateTaskRequestDto request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        TaskResponseDto response = createTaskUseCase.execute(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getUserTasks(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        List<TaskResponseDto> response = getUserTasksUseCase.execute(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(
            @PathVariable TaskStatus status,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        List<TaskResponseDto> response = getTasksByStatusUseCase.execute(userId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequestDto request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        TaskResponseDto response = updateTaskStatusUseCase.execute(taskId, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> filterTasksByStatus(
            @RequestParam TaskStatus status,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        List<TaskResponseDto> response = getTasksByStatusUseCase.execute(userId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequestDto request,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        TaskResponseDto response = updateTaskUseCase.execute(taskId, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        deleteTaskUseCase.execute(taskId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}