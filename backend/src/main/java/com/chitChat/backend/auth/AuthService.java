package com.chitChat.backend.auth;

import com.chitChat.backend.auth.dto.LoginRequest;
import com.chitChat.backend.auth.dto.LoginResponse;
import com.chitChat.backend.auth.dto.RegisterRequest;
import com.chitChat.backend.auth.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
