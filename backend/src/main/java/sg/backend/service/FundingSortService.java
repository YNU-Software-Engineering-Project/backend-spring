package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.funding.FundingSortResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.State;
import sg.backend.entity.User;
import sg.backend.repository.FundingLikeRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class FundingSortService {

    private final UserRepository userRepository;
    private final FundingRepository fundingRepository;
    private final FundingLikeRepository fundingLikeRepository;

    @Transactional
    public Page<FundingSortResponseDto> getNewFundings(int page, String email){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            User user = null;
            boolean isAuthenticated;
            if(!email.equals("anonymousUser")) isAuthenticated = true;
            else {
                isAuthenticated = false;
            }
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if(optionalUser.isEmpty()) return Page.empty();
            }

            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByCreatedAtDesc(State.ONGOING, pageable)
                    .map(funding -> {
                        boolean likedByCurrentUser = false;
                        if(isAuthenticated) {
                            likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFundingId())).isPresent();
                        }
                        return new FundingSortResponseDto(funding, likedByCurrentUser);
                    });
        }
    }

    @Transactional
    public List<FundingSortResponseDto> getTop3PopularFundings(String email) {
        User user = null;
        boolean isAuthenticated;
        if (!email.equals("anonymousUser")) isAuthenticated = true;
        else {
            isAuthenticated = false;
        }
        if (isAuthenticated) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) return Collections.emptyList();
        }

        return fundingRepository.findTop3ByCurrentOrderByTotalLikesDesc(State.ONGOING)
                .stream()
                .map(funding -> {
                    boolean likedByCurrentUser = false;
                    if(isAuthenticated) {
                        likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFundingId())).isPresent();
                    }
                    return new FundingSortResponseDto(funding, likedByCurrentUser);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<FundingSortResponseDto> getSmallFundings(int page, String email){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            User user = null;
            boolean isAuthenticated;
            if(!email.equals("anonymousUser")) isAuthenticated = true;
            else {
                isAuthenticated = false;
            }
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if(optionalUser.isEmpty()) return Page.empty();
            }

            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByTargetAmountAsc(State.ONGOING, pageable)
                    .map(funding -> {
                        boolean likedByCurrentUser = false;
                        if(isAuthenticated) {
                            likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFundingId())).isPresent();
                        }
                        return new FundingSortResponseDto(funding, likedByCurrentUser);
                    });
        }
    }

    @Transactional
    public Page<FundingSortResponseDto> getHighAchievementFundings(int page,String email){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            User user = null;
            boolean isAuthenticated;
            if(!email.equals("anonymousUser")) isAuthenticated = true;
            else {
                isAuthenticated = false;
            }
            if(isAuthenticated) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if(optionalUser.isEmpty()) return Page.empty();
            }

            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByAchievementRateDesc(State.ONGOING, pageable)
                    .map(funding -> {
                        boolean likedByCurrentUser = false;
                        if(isAuthenticated) {
                            likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFundingId())).isPresent();
                        }
                        return new FundingSortResponseDto(funding, likedByCurrentUser);
                    });
        }
    }
}
