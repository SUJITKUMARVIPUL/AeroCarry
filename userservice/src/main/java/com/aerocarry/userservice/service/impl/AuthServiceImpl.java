package com.aerocarry.userservice.service.impl;

import com.aerocarry.userservice.dto.AuthDto;
import com.aerocarry.userservice.entity.Role;
import com.aerocarry.userservice.entity.User;
import com.aerocarry.userservice.exception.ResourceNotFoundException;
import com.aerocarry.userservice.repository.UserRepository;
import com.aerocarry.userservice.service.AuthService;
import com.aerocarry.userservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // We will create this utility class next

    @Override
    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER) // Default role
                .build();

        userRepository.save(user);

        // Generate JWT Token
        String jwtToken = jwtService.generateToken(user);

        return AuthDto.AuthResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        // Check if password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String jwtToken = jwtService.generateToken(user);

        return AuthDto.AuthResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}