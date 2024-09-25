package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.FundingInfoRequestDto;
import sg.backend.dto.response.ModifyContentResponseDto;
import sg.backend.service.implement.FundingInfoService;

@RestController
@RequiredArgsConstructor
public class RegisterFundingController {

    private final FundingInfoService fundingInfoService;

    @PostMapping("api/register/info")
    public ResponseEntity<? super ModifyContentResponseDto> register_funding(
            @RequestBody @Valid FundingInfoRequestDto requestBody
            ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingInfoService.modifyInfo(null,requestBody,true);
        return response;
    }
}
