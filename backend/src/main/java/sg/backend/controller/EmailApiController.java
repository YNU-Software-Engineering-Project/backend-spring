package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.user.EmailVerificationResponseDto;
import sg.backend.service.EmailService;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EmailApiController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = @Content(schema = @Schema(implementation = EmailVerificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 유효하지 않은 토큰 
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/confirm-email")
    public ResponseEntity<? super EmailVerificationResponseDto> confirmEmail(
            @RequestParam @Valid String token
    ) {
        ResponseEntity<? super EmailVerificationResponseDto> response = emailService.verifyEmail(token);
        return response;
    }
}
