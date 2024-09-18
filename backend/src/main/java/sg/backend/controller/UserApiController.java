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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.dto.response.user.PatchPhoneNumberResponseDto;
import sg.backend.dto.response.user.PatchUserProfileResponseDto;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @Operation(
            summary = "사용자 프로필 조회",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 조회 성공",
                content = @Content(schema = @Schema(implementation = GetUserProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<? super GetUserProfileResponseDto> getUserProfile(
            @AuthenticationPrincipal Long userId
    ) {
        ResponseEntity<? super GetUserProfileResponseDto> response = userService.getUserProfile(userId);
        return response;
    }

    @Operation(
            summary = "사용자 전화번호 수정",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 수정 성공",
                    content = @Content(schema = @Schema(implementation = PatchPhoneNumberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/modify-phone-number")
    public ResponseEntity<? super PatchPhoneNumberResponseDto> modifyPhoneNumber(
            @RequestBody @Valid PatchPhoneNumberRequestDto requestBody,
            @AuthenticationPrincipal Long userId
    ) {
        ResponseEntity<? super PatchPhoneNumberResponseDto> response = userService.modifyPhoneNumber(requestBody, userId);
        return response;
    }

    @Operation(
            summary = "사용자 프로필 수정",
            security = @SecurityRequirement(name = "bearerToken")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 수정 성공",
                    content = @Content(schema = @Schema(implementation = PatchUserProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 중복된 닉네임
                    - 유효성 검사 실패
                    - 새 비밀번호가 현재 비밀번호와 같음 
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/modify-profile")
    public ResponseEntity<? super PatchUserProfileResponseDto> modifyProfile(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "userInfo") @Valid PatchUserProfileRequestDto userInfo,
            @AuthenticationPrincipal Long userId
    ) {
        ResponseEntity<? super PatchUserProfileResponseDto> response = userService.modifyProfile(profileImage, userInfo, userId);
        return response;
    }
}
