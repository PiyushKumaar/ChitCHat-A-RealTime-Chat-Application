package com.chitChat.backend.service;

import com.chitChat.backend.dto.request.UserRequest;
import com.chitChat.backend.dto.response.UserResponse;
import com.chitChat.backend.dto.response.UsernameUpdateResponse;
import com.chitChat.backend.dto.response.PageResponse;

public interface UserService {

    UserResponse getCurrentUser(String username) ;

    PageResponse<UserResponse> searchUsers(int page, int size, String search);

    UserResponse editUsers(String username, UserRequest userRequest);

    UsernameUpdateResponse updateUsername(String currentUsername, String newUsername);

    UserResponse updateBio(String username, String bio);
}
