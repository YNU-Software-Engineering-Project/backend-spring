package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.auth.PasswordResetRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.PasswordResetResponseDto;
import sg.backend.service.PasswordResetService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "임시 비밀번호 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자",
                    content = @Content(schema = @Schema(implementation = PasswordResetResponseDto.class)))
    })

    @PostMapping("/reset")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody PasswordResetRequestDto requestDto){
        return passwordResetService.resetPassword(requestDto);
    }
}