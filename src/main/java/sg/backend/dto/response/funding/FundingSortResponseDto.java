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
    private String profileImage;
    private String title;
    private String mainImage;
    private int achievementRate;
    private LocalDateTime createdAt;
    private State current;
    private String details;
    private List<String> tag;
    private boolean likedByCurrentUser;

    public FundingSortResponseDto(Funding funding, boolean likedByCurrentUser){
        this.fundingId = funding.getFunding_id();
        this.title = funding.getTitle();
        this. profileImage = funding.getUser().getProfileImage();
        this.mainImage = funding.getMainImage();
        this.achievementRate = getAchievementRate(funding);
        this.createdAt = funding.getCreatedAt();
        this.current = funding.getCurrent();
        this.details = funding.getProjectSummary();
        List<String> tag = funding.getTagList().stream()
                .map(Tag::getTag_name)
                .collect(Collectors.toList());
        this.tag = tag;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    private int getAchievementRate(Funding funding) {
        int achievementRate = funding.getCurrentAmount() == 0 ? 0 :
                (int) (((double) funding.getCurrentAmount() / funding.getTargetAmount()) * 100);
        return achievementRate;
    }
}
