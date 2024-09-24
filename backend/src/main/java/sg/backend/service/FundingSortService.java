package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.funding.FundingSortResponseDto;
import sg.backend.entity.State;
import sg.backend.repository.FundingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundingSortService {

    private final FundingRepository fundingRepository;

    public Page<FundingSortResponseDto> getNewFundings(int page){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByCreatedAtDesc(State.ONGOING, pageable)
                    .map(FundingSortResponseDto::new);
        }
    }

    public List<FundingSortResponseDto> getTop3PopularFundings(){
        return fundingRepository.findTop3ByCurrentOrderByTotalLikesDesc(State.ONGOING)
                .stream()
                .map(FundingSortResponseDto::new)
                .collect(Collectors.toList());
    }

    public Page<FundingSortResponseDto> getSmallFundings(int page){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByTargetAmountAsc(State.ONGOING, pageable)
                    .map(FundingSortResponseDto::new);
        }
    }

    public Page<FundingSortResponseDto> getHighAchievementFundings(int page){
        if(page < 0){
            throw new IllegalArgumentException("Page number error");
        } else{
            Pageable pageable = PageRequest.of(page, 3);
            return fundingRepository.findAllByCurrentOrderByAchievementRateDesc(State.ONGOING, pageable)
                    .map(FundingSortResponseDto::new);
        }
    }
}
