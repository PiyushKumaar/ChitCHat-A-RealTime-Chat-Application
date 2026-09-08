package com.chitChat.backend.controller.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
public class UsernameUpdateResponse {

    private String token;
    private UserResponse user;


}
