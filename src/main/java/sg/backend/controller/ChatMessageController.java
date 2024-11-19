package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.chat.ChatMessageResponse;
import sg.backend.dto.response.chat.GetChatMessagesResponse;
import sg.backend.service.ChatMessageService;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @Operation(
            summary = "특정 채팅방에 대한 메시지 가져오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 채팅방에 대한 메시지 가져오기 성공",
                    content = @Content(schema = @Schema(implementation = GetChatMessagesResponse.class))),
    })
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<GetChatMessagesResponse> getChatMessages(
            @PathVariable Long chatRoomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return chatMessageService.getChatMessages(chatRoomId, pageable);
    }
}
