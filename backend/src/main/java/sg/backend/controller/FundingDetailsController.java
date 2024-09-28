package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.FundingDetailsResponseDto;
import sg.backend.service.FundingDetailsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fundings")
public class FundingDetailsController {

    private final FundingDetailsService fundingDetailsService;

    @Operation(
            summary = "펀딩 스토리 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스토리 조회 성공",
                    content = @Content(schema = @Schema(implementation = FundingDetailsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "스토리를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}/story")
    public FundingDetailsResponseDto getFundingStory(@PathVariable Long fundingId){
        return fundingDetailsService.getFundingStory(fundingId);
    }

    @Operation(
            summary = "펀딩 환불 정책 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불 정책 조회 성공",
                    content = @Content(schema = @Schema(implementation = FundingDetailsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "펀딩을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{funding_id}/refund-policy")
    public FundingDetailsResponseDto getfundPolicy(@PathVariable Long fundingId){
        return fundingDetailsService.getRefundPolicy(fundingId);
    }

    @Operation(
            summary = "펀딩 리워드 정보 조회",
            security = @SecurityRequirement(name = "Bearer 토큰 값")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = FundingDetailsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "펀딩을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{funding_id}/rewards")
    public FundingDetailsResponseDto getRewardInfo(@PathVariable Long fundingId){
        return fundingDetailsService.getRewardInfo(fundingId);
    }
}