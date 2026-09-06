package com.chitChat.backend.service;

import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.dto.PageResponse;

public interface UserService {

    UserResponse getCurrentUser(String username) ;

    PageResponse<UserResponse> searchUsers(int page, int size, String search);
}
