package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.email.EmailSendTokenRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.email.EmailSendTokenResponseDto;
import sg.backend.service.EmailService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final EmailService emailService;

    @Operation(
            summary = "이메일 인증 토큰 요청",
            description = "사용자가 이메일 인증 토큰을 요청합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 토큰 발송 성공",
                    content = @Content(schema = @Schema(implementation = EmailSendTokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/email-verification")
    public ResponseEntity<? super EmailSendTokenResponseDto> sendEmailToken(
            @RequestBody @Valid EmailSendTokenRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super EmailSendTokenResponseDto> response = emailService.createEmailToken(requestBody, email);
        return response;
    }
}
