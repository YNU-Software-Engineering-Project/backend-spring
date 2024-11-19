package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.chat.GetChatRoomListDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.service.ChatRoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @Operation(
            summary = """
                    채팅방 조회
                    (사용자 - 관리자와의 채팅방을 가져옴,
                    관리자 - 메시지가 존재하는 사용자들에 대해 채팅방을 가져옴)
                    """,
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetChatRoomListDto.class))),
    })
    @GetMapping
    public ResponseEntity<GetChatRoomListDto> getChatRoomList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return chatRoomService.getChatRoomList(email, pageable);
    }
}

