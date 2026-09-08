package com.chitChat.backend.service;

import com.chitChat.backend.controller.user.dto.UserRequest;
import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.controller.user.dto.UsernameUpdateResponse;
import com.chitChat.backend.dto.PageResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface UserService {

    UserResponse getCurrentUser(String username) ;

    PageResponse<UserResponse> searchUsers(int page, int size, String search);

    UserResponse editUsers(String username, UserRequest userRequest);

    UsernameUpdateResponse updateUsername(String currentUsername, String newUsername);

    UserResponse updateBio(String username, String bio);
}
