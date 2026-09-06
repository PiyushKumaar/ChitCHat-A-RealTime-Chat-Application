package com.chitChat.backend.controller.user;

import com.chitChat.backend.controller.user.dto.UserResponse;
import com.chitChat.backend.dto.PageResponse;
import com.chitChat.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getCurrentUser(username));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String search){
        return ResponseEntity.ok(userService.searchUsers(page, size,search));
    }

}
