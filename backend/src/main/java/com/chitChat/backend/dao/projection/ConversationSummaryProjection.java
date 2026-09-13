package com.chitChat.backend.dao.projection;

import com.chitChat.backend.entity.conversation.Conversation;
import com.chitChat.backend.entity.user.User;

public interface ConversationSummaryProjection {

    Conversation getConversation();

    User getOtherUser();
}
