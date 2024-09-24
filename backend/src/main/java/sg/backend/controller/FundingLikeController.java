package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.user.FundingLikeRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.FundingSortResponseDto;
import sg.backend.service.FundingLikeService;

@RestController
@RequestMapping("api/funding/like")
@RequiredArgsConstructor
public class FundingLikeController {

    private final FundingLikeService fundingLikeService;

    @Operation(
            summary = "펀딩게시글 좋아요 기능"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "하트 누르기를 통한 좋아요 등록/취소 기능",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/toggle")
    public ResponseEntity<ResponseDto> toggleFundingLike(@RequestBody FundingLikeRequestDto requestDto){
        ResponseEntity<ResponseDto> responseDto = fundingLikeService.toggleFundingLike(requestDto);
        return responseDto;
    }
}
