package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.funding.RewardListResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Reward;
import sg.backend.repository.RewardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardListService {

    private final RewardRepository rewardRepository;

    public List<RewardListResponseDto> getRewardsByFunding(Funding funding) {
        List<Reward> rewards = rewardRepository.findAllByFunding(funding);
        return rewards.stream()
                .map(RewardListResponseDto::new)
                .collect(Collectors.toList());
    }
}
