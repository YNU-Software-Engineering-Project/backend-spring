package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.AdminSummaryDto;
import sg.backend.dto.response.funding.GetFundingByStateResponseDto;
import sg.backend.service.AdminSummaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminSummaryController {

    private final AdminSummaryService adminSummaryService;

    @Operation(
            summary = "관리자 요약 페이지 불러오기"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요약 요소들 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = AdminSummaryDto.class)))
    })
    @GetMapping("/summary")
    public ResponseEntity<AdminSummaryDto> getAdminSummary(){
        AdminSummaryDto summary = adminSummaryService.getAdminSummary();
        return ResponseEntity.ok(summary);
    }


}
