package sg.backend.dto.response.funding;

import lombok.Getter;
import lombok.Setter;
import sg.backend.entity.Funding;
import sg.backend.entity.State;
import sg.backend.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private Integer rewardAmount;
    private String details;
    private List<String> tags;
    private boolean likedByCurrentUser;

    public FundingSortResponseDto(Funding funding, boolean likedByCurrentUser){
        this.fundingId = funding.getFunding_id();
        this.title = funding.getTitle();
        this.targetAmount = funding.getTargetAmount();
        this.currentAmount = funding.getCurrentAmount();
        this.totalLikes = funding.getTotalLikes();
        this.createdAt = funding.getCreatedAt();
        this.current = funding.getCurrent();
        this.rewardAmount = funding.getRewardAmount();
        this.details = funding.getProjectSummary();
        List<String> tags = funding.getTagList().stream()
                .map(Tag::getTag_name)
                .collect(Collectors.toList());
        this.tags = tags;
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
