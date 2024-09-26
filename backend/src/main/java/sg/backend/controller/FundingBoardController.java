package sg.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.funding.FundingDashboardResponseDto;
import sg.backend.service.FundingBoardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/funding")
public class FundingBoardController {

    private final FundingBoardService fundingBoardService;

    @GetMapping("/{fundingId}/dashboard")
    public ResponseEntity<FundingDashboardResponseDto> getFundingDashboard(@PathVariable Long fundingId){
        FundingDashboardResponseDto dashboard = fundingBoardService.getFundingDashboard(fundingId);
        return ResponseEntity.ok(dashboard);
    }
}
