package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import sg.backend.dto.response.funding.GetFundingByStateResponseDto;
import sg.backend.dto.response.funding.GetFundingStateCountResponseDto;
import sg.backend.dto.response.user.GetUserInfoResponseDto;
import sg.backend.dto.response.user.GetUserListResponseDto;
import sg.backend.service.FundingService;
import sg.backend.service.UserService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FundingService fundingService;
    private final UserService userService;

    @Operation(
            summary = "펀딩 상태에 따른 게시물 수 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 상태에 따른 게시물 수 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingStateCountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings/state")
    public ResponseEntity<GetFundingStateCountResponseDto> getFundingStateCount(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        return fundingService.getFundingStateCount(email);
    }

    @Operation(
            summary = "펀딩 상태에 따른 게시물 리스트 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 상태에 따른 게시물 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingByStateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings")
    public ResponseEntity<GetFundingByStateResponseDto> getFundingByState(
            @AuthenticationPrincipal(expression = "username") String email,
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = "펀딩 상태 (REVIEW: 심사 대기, REVIEW_COMPLETED: 심사 완료, ONGOING: 진행 중)") @RequestParam String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        return fundingService.getFundingByState(email, state, keyword, page, size);
    }

    @Operation(
            summary = "펀딩 상태 변경",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/funding/{fundingId}")
    public ResponseEntity<ResponseDto> changeFundingState(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long fundingId,
            @Parameter(description = "펀딩 상태 (DRAFT: 작성 중, REVIEW: 심사 대기, REVIEW_COMPLETED: 심사 완료, ONGOING: 진행 중, CLOSED: 종료)")
            @RequestParam String state
    ) {
        return fundingService.changeFundingState(email, fundingId, state);
    }

    @Operation(
            summary = "회원 명단 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 명단 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetUserListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/users")
    public ResponseEntity<GetUserListResponseDto> getUserList(
            @AuthenticationPrincipal(expression = "username") String email,
            @Parameter(description = """
                    noAsc: 번호 오름차순, noDesc: 번호 내림차순,
                    idAsc: 아이디 오름차순, idDesc: 아이디 내림차순,
                    nicknameAsc: 닉네임 오름차순, nicknameDesc: 닉네임 내림차순,
                    emailAsc: 이메일 오름차순, emailAsc: 이메일 내림차순,
                    phoneNumAsc: 전화번호 오름차순, phoneNumDesc: 전화번호 내림차순,
                    adAsc: 주소 오름차순, adDesc: 주소 내림차순,
                    latest: 최근 가입 순, oldest: 가입 오래된 순
            """)
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @Parameter(description = "아이디로 회원 검색")
            @RequestParam(required = false) String id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return userService.getUserList(email, sort, id, page, size);
    }

    @Operation(
            summary = "특정 회원 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 회원 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetUserInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<GetUserInfoResponseDto> getUserList(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long userId
    ) {
        return userService.getUserInfo(email, userId);
    }

    @Operation(
            summary = "회원 상태 변경",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/user/{userId}")
    public ResponseEntity<ResponseDto> changeUserState(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long userId,
            @Parameter(description = "회원 상태 (USER: 일반 회원, SUSPENDED: 정지 회원)")
            @RequestParam String role
    ) {
        return userService.changeUserState(email, userId, role);
    }
}
