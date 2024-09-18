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
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 토큰 발송 성공",
                    content = @Content(schema = @Schema(implementation = EmailSendTokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 대학교 메일 주소가 아닌 경우 
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/email-verification")
    public ResponseEntity<? super EmailSendTokenResponseDto> sendEmailToken(
            @RequestBody @Valid EmailSendTokenRequestDto requestBody,
            @AuthenticationPrincipal Long userId
    ) {
        ResponseEntity<? super EmailSendTokenResponseDto> response = emailService.createEmailToken(requestBody, userId);
        return response;
    }
}
