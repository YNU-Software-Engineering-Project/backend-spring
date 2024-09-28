package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.funding.FundingDetailsResponseDto;
import sg.backend.entity.Funding;
import sg.backend.repository.FundingRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingDetailsService {

    private final FundingRepository fundingRepository;

    public FundingDetailsResponseDto getRefundPolicy(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ResponseStatusException(ResponseDto.noExistFunding().getStatusCode(),"Funding not found"));

        return new FundingDetailsResponseDto(
                null,
                funding.getRefundPolicy(),
                funding.getTargetAmount(),
                funding.getCurrentAmount(),
                calculateAchievementRate(funding),
                getRemainingDays(funding.getEndDate()),
                getSupporterCount(funding.getFunding_id()),
                null
        );
    }

    public FundingDetailsResponseDto getRewardInfo(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new ResponseStatusException(ResponseDto.noExistFunding().getStatusCode(),"Funding not found"));

        return new FundingDetailsResponseDto(
                funding.getRewardInfo(),
                null,
                funding.getTargetAmount(),
                funding.getCurrentAmount(),
                calculateAchievementRate(funding),
                getRemainingDays(funding.getEndDate()),
                getSupporterCount(funding.getFunding_id()),
                null
        );
    }

    public FundingDetailsResponseDto getFundingStory(Long fundingId){
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(()-> new ResponseStatusException(ResponseDto.noExistFunding().getStatusCode(), "Funding not found"));

        funding.setTotalVisitors(funding.getTotalVisitors() + 1);
        funding.setTodayVisitors(funding.getTodayVisitors() + 1);
        fundingRepository.save(funding);

        return new FundingDetailsResponseDto(
                null,
                null,
                funding.getTargetAmount(),
                funding.getCurrentAmount(),
                calculateAchievementRate(funding),
                getRemainingDays(funding.getEndDate()),
                getSupporterCount(funding.getFunding_id()),
                funding.getStory()
        );
    }

    private Double calculateAchievementRate(Funding funding) {
        return (double) funding.getCurrentAmount() / funding.getTargetAmount() * 100;
    }

    private Integer getRemainingDays(LocalDateTime endDate){
        Integer remainday = (endDate!= null) ? (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endDate) : 0;
        return remainday;
    }

    private Long getSupporterCount(Long fundingId){
        return fundingRepository.countFundersByFunding_id(fundingId);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetTodayVisitor(){
        List<Funding> fundings = fundingRepository.findAll();
        for(Funding funding : fundings){
            funding.setTodayVisitors(0);
        }
        fundingRepository.saveAll(fundings);
    }
}
