package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController{

    private final UserService userService;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 이메일 중복 또는 비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        return userService.signup(signUpRequestDto);
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginRequestDto.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 이메일 또는 비밀번호",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }
}