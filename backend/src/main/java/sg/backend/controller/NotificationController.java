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
import sg.backend.dto.response.notification.DeleteNotificationResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationsResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "알림 목록 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetNotificationsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetNotificationsResponseDto> response = notificationService.getNotifications(userId, page, size);
        return response;
    }

    @Operation(
            summary = "모든 알림 삭제",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteNotificationsResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("")
    public ResponseEntity<? super DeleteNotificationsResponseDto> deleteNotifications(
            @AuthenticationPrincipal Long userId
    ) {
        ResponseEntity<? super DeleteNotificationsResponseDto> response = notificationService.deleteNotifications(userId);
        return response;
    }

    @Operation(
            summary = "특정 알림 삭제",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteNotificationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 존재하지 않는 알림
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId
    ) {
        ResponseEntity<? super DeleteNotificationResponseDto> response = notificationService.deleteNotification(userId, notificationId);
        return response;
    }
}
