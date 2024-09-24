package sg.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.user.FundingLikeRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.service.FundingLikeService;

@RestController
@RequestMapping("api/funding/like")
@RequiredArgsConstructor
public class FundingLikeController {

    private final FundingLikeService fundingLikeService;

    @PostMapping("/toggle")
    public ResponseEntity<ResponseDto> toggleFundingLike(@RequestBody FundingLikeRequestDto requestDto){
        ResponseEntity<ResponseDto> responseDto = fundingLikeService.toggleFundingLike(requestDto);
        return responseDto;
    }
}
