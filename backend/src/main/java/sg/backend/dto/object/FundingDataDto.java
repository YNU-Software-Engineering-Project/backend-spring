package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FundingDataDto {
    private Long fundingId;
    private String title;
    private String mainImage;
    private String projectSummary;
    private int price;
    private String category;
    private List<String> tag;
    private int achievementRate;
    private boolean isLiked;
    private String state;
}
