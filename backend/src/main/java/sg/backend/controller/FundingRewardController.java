package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.MakeRewardRequestDto;
import sg.backend.dto.request.PolicyRefundRequestDto;
import sg.backend.dto.request.PolicyRewardRequestDto;
import sg.backend.dto.response.*;
import sg.backend.service.implement.FundingRewardService;

@RestController
@RequestMapping("/api/user/fundings")
@RequiredArgsConstructor
public class FundingRewardController {

    private final FundingRewardService fundingRewardService;

    @GetMapping("/{funding_id}/reward")
    public ResponseEntity<? super GetMRewardResponseDto> getReward(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetMRewardResponseDto> response = fundingRewardService.getReward(funding_id);
        return response;
    }

    @PostMapping("/{funding_id}/reward/insert")
    public ResponseEntity<? super MakeRewardResponseDto> insertReward(
            @PathVariable Long funding_id,
            @RequestBody @Valid MakeRewardRequestDto requestBody
    ){
        ResponseEntity<? super MakeRewardResponseDto> response = fundingRewardService.insertReward(funding_id, requestBody);
        return response;
    }

    @DeleteMapping("/reward/{reward_id}/delete")
    public ResponseEntity<? super DeleteDataResponseDto> deleteReward(
            @PathVariable Long reward_id
    ){
        ResponseEntity<? super DeleteDataResponseDto> response = fundingRewardService.deleteReward(reward_id);
        return response;
    }

    @GetMapping("/{funding_id}/policy")
    public ResponseEntity<? super GetPolicyResponseDto> getPolicy(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super GetPolicyResponseDto> response = fundingRewardService.getPolicy(funding_id);
        return response;
    }

    @PostMapping("/{funding_id}/policy/refund")
    public ResponseEntity<? super ModifyContentResponseDto> insertRefundPolicy(
            @PathVariable Long funding_id,
            @RequestBody @Valid PolicyRefundRequestDto requestBody
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.insertRefundPolicy(funding_id, requestBody);
        return response;
    }

    @PostMapping("/{funding_id}/policy/reward")
    public ResponseEntity<? super ModifyContentResponseDto> insertRewardIInfo(
            @PathVariable Long funding_id,
            @RequestBody @Valid PolicyRewardRequestDto requestBody
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.insertRewardInfo(funding_id, requestBody);
        return response;
    }

    @PostMapping("/{funding_id}/submit")
    public ResponseEntity<? super ModifyContentResponseDto> submit_funding(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.submit_funding(funding_id);
        return response;
    }

    @PostMapping("/{funding_id}/giveup")
    public ResponseEntity<? super ModifyContentResponseDto> giveup_funding(
            @PathVariable Long funding_id
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingRewardService.giveup_funding(funding_id);
        return response;
    }
}
