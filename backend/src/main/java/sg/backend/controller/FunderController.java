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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFunderListResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.dto.response.funding.GetRewardListResponseDto;
import sg.backend.service.FunderService;
import sg.backend.service.FundingService;

import java.util.List;

@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FunderController {

    private final FunderService funderService;
    private final FundingService fundingService;

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
    public ResponseEntity<? super GetFunderListResponseDto> getFunderList(
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
            @Parameter(description = "리워드 옵션 번호", example = "rewardIndex=0")
            @RequestParam(required = false) Integer rewardNo,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        ResponseEntity<? super GetFunderListResponseDto> response = funderService.getFunderList(email, fundingId, sort, id, rewardNo, page, size);
        return response;
    }

    @Operation(
            summary = "특정 펀딩 게시물의 리워드 목록 가져오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 펀딩 게시물의 리워드 목록 가져오기 성공",
                    content = @Content(schema = @Schema(implementation = GetRewardListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시물 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{fundingId}/rewards")
    public ResponseEntity<? super GetRewardListResponseDto> getRewardList(
            @PathVariable Long fundingId
    ) {
        ResponseEntity<? super GetRewardListResponseDto> response = fundingService.getRewardList(fundingId);
        return response;
    }
}
