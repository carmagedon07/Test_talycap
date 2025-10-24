package com.taskflow.infrastructure.controllers;

import com.taskflow.application.dtos.UserProfileDto;
import com.taskflow.application.usecases.GetUserProfileUseCase;
import com.taskflow.infrastructure.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final TokenService tokenService;
    
    public UserController(GetUserProfileUseCase getUserProfileUseCase,
                         TokenService tokenService) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.tokenService = tokenService;
    }
    
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = tokenService.extractTokenFromAuthHeader(authHeader);
        Long userId = tokenService.extractUserIdFromToken(token);
        
        UserProfileDto response = getUserProfileUseCase.execute(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}