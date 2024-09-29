package sg.backend.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FundingDataDto {
    private Long fundingId;
    private String profileImage;
    private String title;
    private String mainImage;
    private String projectSummary;
    private List<String> tag;
    private int achievementRate;
    private boolean isLiked;
    private String state;
}
