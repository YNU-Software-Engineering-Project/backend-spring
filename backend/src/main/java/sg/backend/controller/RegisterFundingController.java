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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.wirtefunding.FundingInfoRequestDto;
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.writefunding.RegisterResponseDto;
import sg.backend.service.FundingInfoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RegisterFundingController {

    private final FundingInfoService fundingInfoService;

    @Operation(
            summary = "학교 이메일을 가진 사람만 등록하기 가능"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학교이메일이 있는 경우",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "학교 이메일이 없는 경우",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("api/register")
    public ResponseEntity<? super RegisterResponseDto> register(
            @AuthenticationPrincipal(expression = "username") String email
    ){
        ResponseEntity<? super RegisterResponseDto> response = fundingInfoService.register(email);
        return response;
    }


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
    public ResponseEntity<? super RegisterResponseDto> register_funding(
            @RequestBody @Valid FundingInfoRequestDto requestBody,
            @AuthenticationPrincipal(expression = "username") String email
    ){
        ResponseEntity<? super RegisterResponseDto> response = fundingInfoService.modifyInfo(null,requestBody, email,true);
        return response;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)  //이메일 유효성 검사 처리
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorMessages);
    }
}
