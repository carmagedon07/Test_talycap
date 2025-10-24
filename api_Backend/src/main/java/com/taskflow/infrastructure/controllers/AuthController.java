package com.taskflow.infrastructure.controllers;

import com.taskflow.application.dtos.AuthResponseDto;
import com.taskflow.application.dtos.UserRegistrationResponseDto;
import com.taskflow.application.dtos.LoginRequestDto;
import com.taskflow.application.dtos.RegisterRequestDto;
import com.taskflow.application.usecases.LoginUserUseCase;
import com.taskflow.application.usecases.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    
    public AuthController(RegisterUserUseCase registerUserUseCase,
                         LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        UserRegistrationResponseDto response = registerUserUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto response = loginUserUseCase.execute(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}