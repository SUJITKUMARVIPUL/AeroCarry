package com.aerocarry.userservice.service;

import com.aerocarry.userservice.dto.AuthDto;

public interface AuthService {
    AuthDto.AuthResponse register(AuthDto.RegisterRequest request);
    AuthDto.AuthResponse login(AuthDto.LoginRequest request);
}