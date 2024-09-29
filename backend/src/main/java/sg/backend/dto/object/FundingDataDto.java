package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
}
