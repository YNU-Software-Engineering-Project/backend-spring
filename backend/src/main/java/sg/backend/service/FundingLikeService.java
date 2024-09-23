package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.object.FundingLikeRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.FundingLike;
import sg.backend.entity.User;
import sg.backend.repository.FundingLikeRepository;

@Service
@RequiredArgsConstructor
public class FundingLikeService {

    private final FundingLikeRepository fundingLikeRepository;

    public ResponseEntity<ResponseDto> toggleFundingLike(FundingLikeRequestDto requestDto){
        boolean exists = fundingLikeRepository.existsByUserId_FundingId(requestDto.getUserId(), requestDto.getFundingId());

        if (exists){
            fundingLikeRepository.deleteByUserId_FundingId(requestDto.getUserId(), requestDto.getFundingId());
            return ResponseDto.success();
        } else{
            FundingLike fundingLike = new FundingLike();
            fundingLike.setUser(new User(requestDto.getUserId()));
            fundingLike.setFunding(new Funding(requestDto.getFundingId()));

            fundingLikeRepository.save(fundingLike);
            return ResponseDto.success();
        }
    }
}
