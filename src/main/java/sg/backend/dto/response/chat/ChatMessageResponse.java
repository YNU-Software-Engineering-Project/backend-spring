package sg.backend.dto.response.chat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {
    private Long chatMessageId;
    private String content;
    private Long senderId;
    private String createdAt;
}