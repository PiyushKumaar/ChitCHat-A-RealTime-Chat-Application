package com.chitChat.backend.controller.user;

import com.chitChat.backend.controller.user.dto.*;
import com.chitChat.backend.dto.PageResponse;
import com.chitChat.backend.service.FileService;
import com.chitChat.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private FileService fileService;

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
        return ResponseEntity.ok(userService.searchUsers(page, size, search));
    }

    @PatchMapping("/edit-user")
    public ResponseEntity<UserResponse> editUsers(Authentication authentication ,
                                                  @RequestBody UserRequest userRequest){
        String username  = authentication.getName();
        return ResponseEntity.ok(userService.editUsers(username,userRequest));
    }

    @PutMapping("/me/username")
    public ResponseEntity<UsernameUpdateResponse> updateUsername(
            Authentication authentication,
            @Valid @RequestBody UsernameRequest request) {

        String currentUsername = authentication.getName();

        return ResponseEntity.ok(
                userService.updateUsername(
                        currentUsername,
                        request.getUsername()
                )
        );
    }

    @PutMapping("/me/bio")
    public ResponseEntity<UserResponse> updateBio(
            Authentication authentication,
            @Valid @RequestBody BioRequest request) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                userService.updateBio(
                        username,
                        request.getBio()
                )
        );
    }

    @PutMapping("/profilePic")
    public ResponseEntity<String> updateProfilePicture(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                fileService.saveProfilePic(username, file)
        );
    }

}
