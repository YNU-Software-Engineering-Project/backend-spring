package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.funding.AdminSummaryDto;
import sg.backend.dto.response.funding.FundingDashboardResponseDto;
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
}
