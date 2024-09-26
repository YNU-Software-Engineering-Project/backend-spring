package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.wirtefunding.MakeRewardRequestDto;
import sg.backend.dto.request.wirtefunding.PolicyRefundRequestDto;
import sg.backend.dto.request.wirtefunding.PolicyRewardRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.writefunding.DeleteDataResponseDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.writefunding.reward.GetMRewardResponseDto;
import sg.backend.dto.response.writefunding.reward.GetPolicyResponseDto;
import sg.backend.dto.response.writefunding.reward.MakeRewardResponseDto;
import sg.backend.service.implement.FundingRewardService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingRewardController {

    private final FundingRewardService fundingRewardService;


    @Operation(summary = "리워드설계 페이지 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 리워드 가져오기 성공",
                    content = @Content(schema = @Schema(implementation = GetMRewardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}/reward")
    public ResponseEntity<? super GetMRewardResponseDto> getReward(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetMRewardResponseDto> response = fundingRewardService.getReward(funding_id);
        return response;
    }

    @Operation(summary = "리워드 추가하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드 추가 성공",
                    content = @Content(schema = @Schema(implementation = MakeRewardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 리워드내용을 다 작성하지 않았을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/reward/insert")
    public ResponseEntity<? super MakeRewardResponseDto> insertReward(
            @PathVariable Long funding_id,
            @RequestBody @Valid MakeRewardRequestDto requestBody
    ){
        ResponseEntity<? super MakeRewardResponseDto> response = fundingRewardService.insertReward(funding_id, requestBody);
        return response;
    }

    @Operation(summary = "리워드 하나 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드 삭제 성공",
                    content = @Content(schema = @Schema(implementation = DeleteDataResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않거나 삭제할 리워드 id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/reward/{reward_id}/delete")
    public ResponseEntity<? super DeleteDataResponseDto> deleteReward(
            @PathVariable Long reward_id
    ){
        ResponseEntity<? super DeleteDataResponseDto> response = fundingRewardService.deleteReward(reward_id);
        return response;
    }

    @Operation(summary = "정책페이지 불러오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정책 불러오기 성공",
                    content = @Content(schema = @Schema(implementation = GetPolicyResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/{funding_id}/policy")
    public ResponseEntity<? super GetPolicyResponseDto> getPolicy(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetPolicyResponseDto> response = fundingRewardService.getPolicy(funding_id);
        return response;
    }

    @Operation(summary = "환불정책 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환불정책 작성 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/policy/refund")
    public ResponseEntity<? super ModifyContentResponseDto> insertRefundPolicy(
            @PathVariable Long funding_id,
            @RequestBody @Valid PolicyRefundRequestDto requestBody
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.insertRefundPolicy(funding_id, requestBody);
        return response;
    }

    @Operation(summary = "리워드정보 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리워드정보 작성 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/policy/reward")
    public ResponseEntity<? super ModifyContentResponseDto> insertRewardIInfo(
            @PathVariable Long funding_id,
            @RequestBody @Valid PolicyRewardRequestDto requestBody
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.insertRewardInfo(funding_id, requestBody);
        return response;
    }

    @Operation(summary = "게시물 제출하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시물 상태변화 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "제출할 funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/submit")
    public ResponseEntity<? super ModifyContentResponseDto> submit_funding(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.submit_funding(funding_id);
        return response;
    }

    @Operation(summary = "게시물 포기하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포기할 게시물 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "삭제할 funding_id가 존재하지 않을 때",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/{funding_id}/giveup")
    public ResponseEntity<? super ModifyContentResponseDto> giveup_funding(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.giveup_funding(funding_id);
        return response;
    }
}
