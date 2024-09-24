package sg.backend.dto.response.funding;

import lombok.Getter;
import lombok.Setter;
import sg.backend.entity.Funding;
import sg.backend.entity.State;

import java.time.LocalDateTime;

@Getter
@Setter
public class FundingSortResponseDto {
    private Long fundingId;
    private String title;
    private Integer targetAmount;
    private Integer currentAmount;
    private Integer totalLikes;
    private LocalDateTime createdAt;
    private State current;

    public FundingSortResponseDto(Funding funding){
        this.fundingId = funding.getFundingId();
        this.title = funding.getTitle();
        this.targetAmount = funding.getTargetAmount();
        this.currentAmount = funding.getCurrentAmount();
        this.totalLikes = funding.getTotalLikes();
        this.createdAt = funding.getCreatedAt();
        this.current = funding.getCurrent();
    }
}
