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
import sg.backend.dto.request.email.EmailSendTokenRequestDto;
import sg.backend.dto.request.user.PatchPhoneNumberRequestDto;
import sg.backend.dto.request.user.PatchUserProfileRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.email.EmailSendTokenResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.dto.response.user.PatchPhoneNumberResponseDto;
import sg.backend.dto.response.user.PatchUserProfileResponseDto;
import sg.backend.entity.User;
import sg.backend.service.EmailService;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Operation(
            summary = "사용자 프로필 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetUserProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<? super GetUserProfileResponseDto> getUserProfile(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        ResponseEntity<? super GetUserProfileResponseDto> response = userService.getUserProfile(email);
        return response;
    }

    @Operation(
            summary = "사용자 전화번호 수정",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
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
            @AuthenticationPrincipal(expression = "username") String email
    ){
        ResponseEntity<? super PatchPhoneNumberResponseDto> response = userService.modifyPhoneNumber(requestBody, email);
        return response;
    }

    @Operation(
            summary = "학교 메일 인증 요청",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 토큰 발송 성공",
                    content = @Content(schema = @Schema(implementation = EmailSendTokenResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 대학교 메일 주소(ac.kr)가 아닌 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/email-verification")
    public ResponseEntity<? super EmailSendTokenResponseDto> sendEmailToken(
            @RequestBody @Valid EmailSendTokenRequestDto requestBody,
            @AuthenticationPrincipal(expression = "username") String email
            ) {
        ResponseEntity<? super EmailSendTokenResponseDto> response = emailService.createEmailToken(requestBody, email);
        return response;
    }

    @Operation(
            summary = "사용자 프로필 수정",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 수정 성공",
                    content = @Content(schema = @Schema(implementation = PatchUserProfileResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 중복된 닉네임
                    - 비밀번호 불일치
                    - 새 비밀번호가 현재 비밀번호와 같음 
                    - 주소란을 일부만 채운 경우(도로명 주소와 지번 주소는 하나만 입력해도 통과)
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/modify-profile")
    public ResponseEntity<? super PatchUserProfileResponseDto> modifyProfile(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "userInfo") @Valid PatchUserProfileRequestDto userInfo,
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        ResponseEntity<? super PatchUserProfileResponseDto> response = userService.modifyProfile(profileImage, userInfo, email);
        return response;
    }

    @Operation(
            summary = "위시리스트 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/wishlist")
    public ResponseEntity<? super GetFundingListResponseDto> getWishList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetFundingListResponseDto> response = userService.getWishList(email, page, size);
        return response;
    }

    @Operation(
            summary = "참여한 펀딩 리스트 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여한 펀딩 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/pledges")
    public ResponseEntity<? super GetFundingListResponseDto> getPledgeList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetFundingListResponseDto> response = userService.getPledgeList(email, page, size);

        return response;
    }

    @Operation(
            summary = "내 펀딩 리스트 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 펀딩 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetMyFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings")
    public ResponseEntity<? super GetMyFundingListResponseDto> getMyFundingList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetMyFundingListResponseDto> response = userService.getMyFundingList(email, page, size);
        return response;
    }

}
