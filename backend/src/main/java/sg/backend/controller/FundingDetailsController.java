package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.FundingDetailsResponseDto;
import sg.backend.dto.response.funding.RewardListResponseDto;
import sg.backend.entity.Funding;
import sg.backend.repository.FundingRepository;
import sg.backend.service.FundingDetailsService;
import sg.backend.service.RewardListService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fundings")
public class FundingDetailsController {

    private final FundingDetailsService fundingDetailsService;
    private final RewardListService rewardListService;
    private final FundingRepository fundingRepository;

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
    public FundingDetailsResponseDto getFundingStory(@PathVariable("funding_id") Long funding_id){
        return fundingDetailsService.getFundingStory(funding_id);
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
    public FundingDetailsResponseDto getfundPolicy(@PathVariable("funding_id") Long funding_id){
        return fundingDetailsService.getRefundPolicy(funding_id);
    }

    @Operation(
            summary = "펀딩 리워드 정보 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = FundingDetailsResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "펀딩을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{funding_id}/rewards")
    public FundingDetailsResponseDto getRewardInfo(@PathVariable("funding_id") Long funding_id){
        return fundingDetailsService.getRewardInfo(funding_id);
    }

    @Operation(
            summary = "펀딩 리워드 리스트 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = RewardListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "펀딩을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("{funding_id}/rewardsList")
    public ResponseEntity<List<RewardListResponseDto>> getRewordsByFundingId(@PathVariable("funding_id") Long funding_id){
        Funding funding = fundingRepository.findById(funding_id)
                .orElseThrow(() ->  new ResponseStatusException(ResponseDto.noExistFunding().getStatusCode(), "Funding not found"));
        List<RewardListResponseDto> rewards = rewardListService.getRewardsByFunding(funding);
        return ResponseEntity.ok(rewards);
    }
}