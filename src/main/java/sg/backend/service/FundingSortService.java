package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public List<FundingSortResponseDto> getNewFundings(String email){
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

        return fundingRepository.findTop3ByCurrentOrderByCreatedAtDesc(State.ONGOING)
                .stream()
                .map(funding -> {
                    boolean likedByCurrentUser = false;
                    if(isAuthenticated) {
                        likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFunding_id())).isPresent();
                    }
                    return new FundingSortResponseDto(funding, likedByCurrentUser);
                })
                .collect(Collectors.toList());
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
                        likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFunding_id())).isPresent();
                    }
                    return new FundingSortResponseDto(funding, likedByCurrentUser);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FundingSortResponseDto> getSmallFundings(String email){
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

        return fundingRepository.findTop3ByCurrentOrderByTargetAmountAsc(State.ONGOING)
                .stream()
                .map(funding -> {
                    boolean likedByCurrentUser = false;
                    if(isAuthenticated) {
                        likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFunding_id())).isPresent();
                    }
                    return new FundingSortResponseDto(funding, likedByCurrentUser);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<FundingSortResponseDto> getHighAchievementFundings(String email){
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

        return fundingRepository.findTop3ByCurrentOrderByAchievementRateDesc(State.ONGOING)
                .stream()
                .map(funding -> {
                    boolean likedByCurrentUser = false;
                    if(isAuthenticated) {
                        likedByCurrentUser = fundingLikeRepository.findByUserAndFunding(user, new Funding(funding.getFunding_id())).isPresent();
                    }
                    return new FundingSortResponseDto(funding, likedByCurrentUser);
                })
                .collect(Collectors.toList());
    }
}
