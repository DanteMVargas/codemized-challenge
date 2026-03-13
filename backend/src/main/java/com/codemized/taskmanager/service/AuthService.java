package com.codemized.taskmanager.service;

import com.codemized.taskmanager.dto.request.LoginRequest;
import com.codemized.taskmanager.dto.request.RegisterRequest;
import com.codemized.taskmanager.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}