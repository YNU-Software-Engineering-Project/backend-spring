package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.funding.FundingSortResponseDto;
import sg.backend.service.FundingSortService;

import java.util.List;

@RestController
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class FundingSortController {

    private final FundingSortService fundingSortService;

    @Operation(
            summary = "신생펀딩 정렬"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최근 등록된 펀딩 순으로 정렬",
                    content = @Content(schema = @Schema(implementation = FundingSortResponseDto.class)))
    })
    @GetMapping("/new")
    public ResponseEntity<Page<FundingSortResponseDto>> getNewFundings(@RequestParam(name = "page") int page){
        return ResponseEntity.ok(fundingSortService.getNewFundings(page));
    }

    @Operation(
            summary = "인기 펀딩 Top3 정렬"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 많은 순으로 정렬",
                    content = @Content(schema = @Schema(implementation = FundingSortResponseDto.class)))
    })
    @GetMapping("/top3")
    public ResponseEntity<List<FundingSortResponseDto>> getTop3PopularFundings(){
        return ResponseEntity.ok(fundingSortService.getTop3PopularFundings());
    }

    @Operation(
            summary = "소액펀딩 정렬"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목표 금액이 적은 순으로 정렬",
                    content = @Content(schema = @Schema(implementation = FundingSortResponseDto.class)))
    })
    @GetMapping("/small")
    public ResponseEntity<Page<FundingSortResponseDto>> getSmallFundings(@RequestParam(name = "page") int page){
        return ResponseEntity.ok(fundingSortService.getSmallFundings(page));
    }

    @Operation(
            summary = "목표달성률 정렬"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(현재금액/목표금액)이 높은 순으로 정렬",
                    content = @Content(schema = @Schema(implementation = FundingSortResponseDto.class)))
    })
    @GetMapping("/achievement")
    public ResponseEntity<Page<FundingSortResponseDto>> getHighAchievementFundings(@RequestParam(name = "page") int page){
        return ResponseEntity.ok(fundingSortService.getHighAchievementFundings(page));
    }
}
