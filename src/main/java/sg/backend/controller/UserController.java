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
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetMyFundingListResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
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
    public ResponseEntity<GetUserProfileResponseDto> getUserProfile(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        return userService.getUserProfile(email);
    }

    @Operation(
            summary = "사용자 전화번호 수정",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전화번호 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/modify-phone-number")
    public ResponseEntity<ResponseDto> modifyPhoneNumber(
            @RequestBody @Valid PatchPhoneNumberRequestDto requestBody,
            @AuthenticationPrincipal(expression = "username") String email
    ){
        return userService.modifyPhoneNumber(requestBody, email);
    }

    @Operation(
            summary = "학교 메일 인증 요청",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 토큰 발송 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 대학교 메일 주소(ac.kr)가 아닌 경우
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/email-verification")
    public ResponseEntity<ResponseDto> sendEmailToken(
            @RequestBody @Valid EmailSendTokenRequestDto requestBody,
            @AuthenticationPrincipal(expression = "username") String email
            ) {
        return emailService.createEmailToken(requestBody, email);
    }

    @Operation(
            summary = "사용자 프로필 수정",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = """
                    잘못된 요청
                    - 존재하지 않는 사용자
                    - 중복된 닉네임
                    - 비밀번호 불일치
                    - 새 비밀번호가 현재 비밀번호와 같음
                    - 주소란을 일부만 채운 경우 (주소란을 모두 비워두거나, 도로명 주소와 지번 주소 중 하나만 제외하고 모두 입력한 경우는 허용된다)
                    """,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/modify-profile")
    public ResponseEntity<ResponseDto> modifyProfile(
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "userInfo") @Valid PatchUserProfileRequestDto userInfo,
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        return userService.modifyProfile(profileImage, userInfo, email);
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
    public ResponseEntity<GetFundingListResponseDto> getWishList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return userService.getWishList(email, page, size);
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
    public ResponseEntity<GetFundingListResponseDto> getPledgeList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return userService.getPledgeList(email, page, size);
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
    public ResponseEntity<GetMyFundingListResponseDto> getMyFundingList(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return userService.getMyFundingList(email, page, size);
    }

}
