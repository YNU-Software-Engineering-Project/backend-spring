package sg.backend.dto.response.funding;

import lombok.Getter;
import lombok.Setter;
import sg.backend.entity.Funding;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
public class FundingDashboardResponseDto {

    private Long fundingId;
    private String title;
    private Integer remainingDays;
    private Integer todayVisitors;
    private Integer totalVisitors;
    private Double visitPercentage;
    private Integer todayLikes;
    private Integer totalLikes;
    private Double likePercentage;
    private Integer currentAmount;
    private Integer targetAmount;
    private Double fundingAchievementRate;

    public FundingDashboardResponseDto(Funding funding){
        this.fundingId = funding.getFunding_id();
        this.title = funding.getTitle();
        this.remainingDays = calculateRemainingDays(funding.getEndDate());

        this.todayVisitors = funding.getTodayVisitors();
        this.totalVisitors = funding.getTotalVisitors();
        this.visitPercentage = (funding.getTotalVisitors() != 0)
                ? (double) funding.getTodayVisitors() / funding.getTotalVisitors() * 100 : 0;

        this.todayLikes = funding.getTodayLikes();
        this.totalLikes = funding.getTotalLikes();
        this.likePercentage = (funding.getTotalLikes() != 0)
                ? (double) funding.getTodayLikes() / funding.getTotalLikes() * 100 : 0;

        this.currentAmount = funding.getCurrentAmount();
        this.targetAmount = funding.getTargetAmount();
        this.fundingAchievementRate = (funding.getTargetAmount() != null && funding.getTargetAmount() != 0)
                ? (double) funding.getCurrentAmount() / funding.getTargetAmount() * 100 : 0;
    }

    private int calculateRemainingDays(LocalDateTime endDate){
        return (endDate != null) ? (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endDate) : 0;
    }
}
