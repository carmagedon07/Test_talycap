package com.taskflow.application.usecases;

import com.taskflow.application.dtos.UserRegistrationResponseDto;
import com.taskflow.application.dtos.RegisterRequestDto;
import com.taskflow.domain.entities.User;
import com.taskflow.domain.exceptions.InvalidCredentialsException;
import com.taskflow.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public RegisterUserUseCase(UserRepository userRepository, 
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public UserRegistrationResponseDto execute(RegisterRequestDto request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidCredentialsException("Email already exists");
        }
        
        // Create new user
        User user = new User(
            request.getName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        
        User savedUser = userRepository.save(user);
        
        // Return user info without token
        return new UserRegistrationResponseDto(
            savedUser.getId(), 
            savedUser.getEmail(), 
            savedUser.getName(),
            "User registered successfully"
        );
    }
}