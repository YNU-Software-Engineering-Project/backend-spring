package sg.backend.dto.request.chat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {
    private String content;
    private Long senderId;
}
