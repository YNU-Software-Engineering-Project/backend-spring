package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.wirtefunding.FundingInfoRequestDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.service.FundingInfoService;

@RestController
@RequiredArgsConstructor
public class RegisterFundingController {

    private final FundingInfoService fundingInfoService;


    @Operation(
            summary = "펀딩 게시물 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "펀딩 게시물 생성 성공",
                    content = @Content(schema = @Schema(implementation = ModifyContentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "name, email, taxemail을 작성하지 않은 경우",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("api/register/info")
    public ResponseEntity<? super ModifyContentResponseDto> register_funding(
            @RequestBody @Valid FundingInfoRequestDto requestBody,
            @AuthenticationPrincipal(expression = "username") String email
    ){
        ResponseEntity<? super ModifyContentResponseDto> response = fundingInfoService.modifyInfo(null,requestBody, email,true);
        return response;
    }
}
