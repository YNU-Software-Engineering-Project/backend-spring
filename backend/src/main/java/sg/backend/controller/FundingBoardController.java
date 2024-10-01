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
import sg.backend.dto.response.funding.FundingDashboardResponseDto;
import sg.backend.dto.response.funding.GetFunderListResponseDto;
import sg.backend.dto.response.funding.GetRewardListResponseDto;
import sg.backend.service.FundingBoardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class FundingBoardController {

    private final FundingBoardService fundingBoardService;

    @Operation(
            summary = "펀딩 상황판 불러오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상황판 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = FundingDashboardResponseDto.class)))
    })
    @GetMapping("/{fundingId}/dashboard")
    public ResponseEntity<FundingDashboardResponseDto> getFundingDashboard(@PathVariable Long fundingId){
        FundingDashboardResponseDto dashboard = fundingBoardService.getFundingDashboard(fundingId);
        return ResponseEntity.ok(dashboard);
    }

    @Operation(
            summary = "후원자 명단 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "후원자 명단 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFunderListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{fundingId}/funders")
    public ResponseEntity<GetFunderListResponseDto> getFunderList(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long fundingId,
            @Parameter(description = """
                    latest: 최근 후원 순, oldest: 가입 오래된 순,
                    idAsc: 아이디 오름차순, idDesc: 아이디 내림차순,
                    nicknameAsc: 닉네임 오름차순, nicknameDesc: 닉네임 내림차순,
                    emailAsc: 이메일 오름차순, emailAsc: 이메일 내림차순,
                    adAsc: 주소 오름차순, adDesc: 주소 내림차순,
                    phoneNumAsc: 전화번호 오름차순, phoneNumDesc: 전화번호 내림차순,
            """)
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @Parameter(description = "아이디로 검색")
            @RequestParam(required = false) String id,
            @Parameter(description = "리워드 옵션 번호", example = "0")
            @RequestParam(required = false) Integer rewardNo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return fundingBoardService.getFunderList(email, fundingId, sort, id, rewardNo, page, size);
    }

    @Operation(
            summary = "펀딩 리워드 이름 목록 가져오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 리워드 이름 목록 가져오기 성공",
                    content = @Content(schema = @Schema(implementation = GetRewardListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시물 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{fundingId}/rewards")
    public ResponseEntity<GetRewardListResponseDto> getRewardList(
            @PathVariable Long fundingId
    ) {
        return fundingBoardService.getRewardList(fundingId);
    }
}
