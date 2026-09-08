package com.chitChat.backend.serviceImpl;

import com.chitChat.backend.controller.user.dto.UserRequest;
import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.controller.user.dto.UsernameUpdateResponse;
import com.chitChat.backend.dao.UserRepository;
import com.chitChat.backend.dto.PageResponse;
import com.chitChat.backend.entity.user.User;
import com.chitChat.backend.exceptions.ResourceNotFoundException;
import com.chitChat.backend.security.CustomUserDetailsService;
import com.chitChat.backend.security.JwtService;
import com.chitChat.backend.service.UserService;
import com.chitChat.backend.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtService jwtService;

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

    @Override
    public UserResponse editUsers(String username, UserRequest userRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new ResourceNotFoundException("user not found iwth username " + username));

        if (userRequest.getFirstName() != null) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            user.setLastName(userRequest.getLastName());
        }
        if(userRequest.getProfileImage()!=null){
            user.setProfileImage(userRequest.getProfileImage());
        }
        if (userRequest.getBio() != null) {
            user.setBio(userRequest.getBio());
        }

        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Override
    public UsernameUpdateResponse updateUsername(String currentUsername, String newUsername) {

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!user.getUsername().equals(newUsername)
                && userRepository.existsByUsername(newUsername)) {
            throw new RuntimeException("Username already exists");
        }

        user.setUsername(newUsername);

        User updatedUser = userRepository.save(user);

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(newUsername);

        String newToken = jwtService.generateToken(userDetails);

        return new UsernameUpdateResponse(
                newToken,
                UserResponse.fromEntity(updatedUser)
        );
    }

    @Override
    public UserResponse updateBio(String username, String bio) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setBio(bio);

        User updatedUser = userRepository.save(user);

        return UserResponse.fromEntity(updatedUser);
    }
}
