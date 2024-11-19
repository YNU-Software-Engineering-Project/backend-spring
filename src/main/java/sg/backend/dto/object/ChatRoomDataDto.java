package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomDataDto {
    private Long chatRoomId;
    private Long counterpartId;
    private String counterpartProfile;
    private ChatMessageDataDto latestMessage;
}
