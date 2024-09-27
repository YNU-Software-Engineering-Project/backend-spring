package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.funding.FundingDashboardResponseDto;
import sg.backend.entity.Funding;
import sg.backend.repository.FundingRepository;

@Service
@RequiredArgsConstructor
public class FundingBoardService {

    private final FundingRepository fundingRepository;

    public FundingDashboardResponseDto getFundingDashboard(Long fundingId){
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(()-> new IllegalArgumentException("해당 펀딩이 존재하지 않습니다."));
        return new FundingDashboardResponseDto(funding);
    }
}
