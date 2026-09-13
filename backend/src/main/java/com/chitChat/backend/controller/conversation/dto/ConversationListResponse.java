package com.chitChat.backend.controller.conversation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationListResponse {

    private List<ConversationSummaryResponse> conversations;
    private String nextCursor;
    private boolean hasMore;
}
