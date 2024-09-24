package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingByStateResponseDto;
import sg.backend.dto.response.funding.GetFundingStateCountResponseDto;
import sg.backend.service.FundingService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final FundingService fundingService;

    @Operation(
            summary = "펀딩 상태에 따른 게시물 수 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 상태에 따른 게시물 수 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingStateCountResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings/state")
    public ResponseEntity<? super GetFundingStateCountResponseDto> getFundingStateCount(
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        ResponseEntity<? super GetFundingStateCountResponseDto> response = fundingService.getFundingStateCount(email);
        return response;
    }

    @Operation(
            summary = "펀딩 상태에 따른 게시물 리스트 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 상태에 따른 게시물 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingByStateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한 없음 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/fundings")
    public ResponseEntity<? super GetFundingByStateResponseDto> getFundingByState(
            @AuthenticationPrincipal(expression = "username") String email,
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = "펀딩 상태 (REVIEW: 심사 대기, REVIEW_COMPLETED: 심사 완료, ONGOING: 진행 중)") @RequestParam String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size
    ) {
        ResponseEntity<? super GetFundingByStateResponseDto> response = fundingService.getFundingByState(email, state, keyword, page, size);
        return response;
    }
}
