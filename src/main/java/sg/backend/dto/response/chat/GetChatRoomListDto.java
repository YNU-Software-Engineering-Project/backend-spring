package sg.backend.dto.response.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.ChatRoomDataDto;

import java.util.List;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.ChatRoom;

@Getter
public class GetChatRoomListDto extends ResponseDto {
    private final List<ChatRoomDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetChatRoomListDto(Page<ChatRoom> chatRooms, List<ChatRoomDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = chatRooms.getNumber();
        this.size = chatRooms.getSize();
        this.totalPages = chatRooms.getTotalPages();
        this.totalElements = chatRooms.getTotalElements();
    }

    public static ResponseEntity<GetChatRoomListDto> success(Page<ChatRoom> page, List<ChatRoomDataDto> data) {
        GetChatRoomListDto result = new GetChatRoomListDto(page, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
