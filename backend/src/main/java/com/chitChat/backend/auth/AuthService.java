package com.chitChat.backend.auth;

import com.chitChat.backend.auth.dto.*;

public interface AuthService {

    MessageResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    MessageResponse changePassword(String username, String currentPassword, String newPassword);
}
