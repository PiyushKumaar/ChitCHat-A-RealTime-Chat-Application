package com.chitChat.backend.serviceImpl;

import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.dto.PageResponse;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.exceptions.ResourceNotFoundException;
import com.chitChat.backend.service.UserService;
import com.chitChat.backend.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found with the username "+username));
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getProfileImage(),
                user.getBio());
    }

    @Override
    public PageResponse<UserResponse> searchUsers(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page,size,"id");

        Page<User> userPage;

        if(search != null && !search.trim().isEmpty()){
            userPage = userRepository.searchUsers(search.trim(),pageable);
        }else {
            userPage = userRepository.findAll(pageable);
        }

        return PaginationUtils.toPageResponse(userPage,UserResponse::fromEntity);
    }
}
