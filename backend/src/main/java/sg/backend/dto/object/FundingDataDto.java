package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.Funding;
import sg.backend.entity.Tag;
import sg.backend.entity.User;
import sg.backend.repository.FundingLikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FundingDataDto {
    private Long fundingId;
    @Schema(description = "회원 프로필 이미지")
    private String profileImage;
    private String title;
    @Schema(description = "게시물 메인 이미지")
    private String mainImage;
    private String projectSummary;
    private List<String> tag;
    private int achievementRate;
    private boolean isLiked;
    private String state;

    public static FundingDataDto of(Funding funding, FundingLikeRepository fundingLikeRepository, boolean isAuthenticated, User user) {
        List<String> tags = funding.getTagList().stream()
                .map(Tag::getTag_name)
                .collect(Collectors.toList());

        int achievementRate = funding.getCurrentAmount() == 0 ? 0 :
                (int) (((double) funding.getCurrentAmount() / funding.getTargetAmount()) * 100);

        return FundingDataDto.builder()
                .profileImage(funding.getUser().getProfileImage())
                .fundingId(funding.getFunding_id())
                .title(funding.getTitle())
                .mainImage(funding.getMainImage())
                .projectSummary(funding.getProjectSummary())
                .tag(tags)
                .achievementRate(achievementRate)
                .isLiked(isAuthenticated && fundingLikeRepository.existsByUserAndFunding(user, funding))
                .state(String.valueOf(funding.getCurrent()))
                .build();
    }
}
