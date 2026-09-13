package com.chitChat.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageListResponse {

    private List<MessageResponse> messages;
    private String nextCursor;
    private boolean hasMore;
}
