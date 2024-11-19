package sg.backend.dto.response.chat;

import lombok.Getter;
import org.springframework.data.domain.Page;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.ChatMessageDataDto;

import java.util.List;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetChatMessagesResponse extends ResponseDto {
    private final List<ChatMessageDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetChatMessagesResponse(Page<?> page, List<ChatMessageDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public static GetChatMessagesResponse success(Page<?> page, List<ChatMessageDataDto> data) {
        return new GetChatMessagesResponse(page, data);
    }
}
