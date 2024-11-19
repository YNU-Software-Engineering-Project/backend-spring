package sg.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.backend.dto.request.user.FundingLikeRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.jwt.TokenProvider;
import sg.backend.service.FundingLikeService;

@RestController
@RequestMapping("api/funding/like")
@RequiredArgsConstructor
public class FundingLikeController {

    private final FundingLikeService fundingLikeService;
    private final TokenProvider tokenProvider;

    @PostMapping("/toggle")
    public ResponseEntity<ResponseDto> toggleFundingLike(
            @RequestBody FundingLikeRequestDto requestDto,
            @RequestHeader("Authorization") String token){
        String parsedToken = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(parsedToken);
        ResponseEntity<ResponseDto> responseDto = fundingLikeService.toggleFundingLike(requestDto, userId);
        return responseDto;
    }
}
