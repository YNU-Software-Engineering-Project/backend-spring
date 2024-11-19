package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageDataDto {
    private Long chatMessageId;
    private String content;
    private Long senderId;
    private String createdAt;
}
