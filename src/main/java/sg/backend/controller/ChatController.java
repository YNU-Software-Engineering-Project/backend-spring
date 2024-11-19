package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import sg.backend.dto.request.chat.ChatMessageRequest;
import sg.backend.dto.response.chat.ChatMessageResponse;
import sg.backend.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;

    @Operation(
            summary = "메시지 전송"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메시지 전송 성공",
                    content = @Content(schema = @Schema(implementation = ChatMessageResponse.class))),
    })
    @MessageMapping("/chat/rooms/{roomId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    public ChatMessageResponse sendMessage(
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest chatMessageRequest
    ) {
        ChatMessageResponse response = chatMessageService.processAndSaveMessage(roomId, chatMessageRequest);
        return response;
    }
}
