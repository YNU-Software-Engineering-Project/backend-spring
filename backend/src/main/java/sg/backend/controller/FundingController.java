package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.GetFundingListResponseDto;
import sg.backend.service.FundingService;

import java.util.List;

@RestController
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class FundingController {

    private final FundingService fundingService;

    @Operation(
            summary = "펀딩 게시물 정렬 및 검색"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 게시물 정렬 및 검색 성공",
                    content = @Content(schema = @Schema(implementation = GetFundingListResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자 또는 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("")
    public ResponseEntity<? super GetFundingListResponseDto> searchFunding(
            @Parameter(description = "검색어") @RequestParam(required = false) String keyword,
            @Parameter(description = """
                    latest: 최신순, oldest: 오래된 순,
                    priceAsc: 가격 높은 순, priceDesc: 가격 낮은 순,
                    achievementRateDesc: 달성률 높은 순, achievementRateAsc: 달성률 낮은 순,
                    deadlineDesc: 마감임박순,
                    likes: 추천순(좋아요 순)
            """)
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @Parameter(description = "카테고리", example = "category=A0010") @RequestParam(required = false) String category,
            @Parameter(description = "태그", example = "tags=art,design") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "최소 후원 금액") @RequestParam(required = false) Long minAmount,
            @Parameter(description = "종료된 펀딩 여부 (true: 종료된 펀딩도 포함)")
            @RequestParam(required = false, defaultValue = "false") Boolean isClosed,
            @Parameter(description = "좋아요한 펀딩 여부 (true: 좋아요한 펀딩도 포함)")
            @RequestParam(required = false, defaultValue = "false") Boolean isLiked,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null ? authentication.getName() : null;

        ResponseEntity<? super GetFundingListResponseDto> response = fundingService.searchFunding(
                email, keyword, sort, category, tags, minAmount, isClosed, isLiked, page, size
        );
        return response;
    }
}
