package sg.backend.dto.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortFundingDataDto {
    private Long fundingId;
    private String title;
    private String mainImage;
    private String state;
}
