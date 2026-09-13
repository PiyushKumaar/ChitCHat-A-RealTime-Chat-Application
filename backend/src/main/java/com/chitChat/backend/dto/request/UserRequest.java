package com.chitChat.backend.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;

    private String lastName;

    private String profileImage;

    private String bio;

}
