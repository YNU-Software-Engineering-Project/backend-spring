package sg.backend.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FundingDataDto {
    private String title;
    private String mainImage;
    private String projectSummary;
    private String category;
    private List<String> tag;
    private int achievementRate;
    private boolean isLike;
    private String state;
}
