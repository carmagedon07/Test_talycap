package com.taskflow.application.usecases;

import com.taskflow.application.dtos.AuthResponseDto;
import com.taskflow.application.dtos.LoginRequestDto;
import com.taskflow.domain.entities.User;
import com.taskflow.domain.exceptions.InvalidCredentialsException;
import com.taskflow.domain.repositories.UserRepository;
import com.taskflow.infrastructure.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    
    public LoginUserUseCase(UserRepository userRepository, 
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }
    
    public AuthResponseDto execute(LoginRequestDto request) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        User user = userOptional.get();
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = tokenService.generateToken(user);
        
        return new AuthResponseDto(token, user.getId(), user.getEmail(), user.getName());
    }
}