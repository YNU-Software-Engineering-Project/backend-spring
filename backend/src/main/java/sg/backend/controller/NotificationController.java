package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "알림 목록 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetNotificationsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetNotificationsResponseDto> response = notificationService.getNotifications(email, page, size);
        return response;
    }

    @Operation(
            summary = "모든 알림 삭제",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("")
    public ResponseEntity<? super ResponseDto> deleteNotifications(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        ResponseEntity<? super ResponseDto> response = notificationService.deleteNotifications(email);
        return response;
    }

    @Operation(
            summary = "특정 알림 삭제",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 존재하지 않는 알림
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<? super ResponseDto> deleteNotification(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long notificationId
    ) {
        ResponseEntity<? super ResponseDto> response = notificationService.deleteNotification(email, notificationId);
        return response;
    }
}
