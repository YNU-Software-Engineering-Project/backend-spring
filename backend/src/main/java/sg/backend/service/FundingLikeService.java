package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.user.FundingLikeRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.FundingLike;
import sg.backend.entity.User;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FundingLikeService {

    private final FundingLikeRepository fundingLikeRepository;
    private final UserRepository userRepository;
    private final FundingRepository fundingRepository;

    @Transactional
    public ResponseEntity<ResponseDto> toggleFundingLike(FundingLikeRequestDto requestDto){
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
        Funding funding = fundingRepository.findById(requestDto.getFundingId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Funding ID"));

        boolean exists = fundingLikeRepository.existsByUserAndFunding(user, funding);

        if (exists){
            fundingLikeRepository.deleteByUserAndFunding(user, funding);
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
