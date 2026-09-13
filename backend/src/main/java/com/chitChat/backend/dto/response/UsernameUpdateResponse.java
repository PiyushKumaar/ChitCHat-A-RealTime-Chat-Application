package com.chitChat.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class UsernameUpdateResponse {

    private String token;
    private UserResponse user;


}
