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
import org.springframework.security.core.parameters.P;
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
            @Parameter(description = """
                            A0010 -> 캐릭터·굿즈, A0020  -> 홈·리빙, A0030 -> 사진, 
                            A0040 -> 게임, A0050 -> 키즈, A0060 -> 도서·전자책,
                            A0070 -> 여행, A0080 -> 만화·웹툰, A0090 -> 스포츠·아웃도어, 
                            A0100 -> 테크·가전, A0110 -> 자동차, A0120 -> 패션, 
                            A0130 -> 아트, A0140 -> 소셜, A0150 -> 영화·음악, 
                            A0160 -> 반려동물, A0170 -> 디자인
                    """, example = "A0010,A0020") @RequestParam(required = false) List<String> categories,
            @Parameter(description = "태그", example = "art,design") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "최소 달성률") @RequestParam(required = false, defaultValue = "0") int minRate,
            @Parameter(description = "최대 달성률") @RequestParam(required = false, defaultValue = "100") int maxRate,
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
                email, keyword, sort, categories, tags, minRate, maxRate, isClosed, isLiked, page, size
        );
        return response;
    }
}
