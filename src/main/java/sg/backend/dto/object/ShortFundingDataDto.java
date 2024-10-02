package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.Funding;

@Getter
@Builder
public class ShortFundingDataDto {
    private Long fundingId;
    private String title;
    private String mainImage;
    private String state;

    public static ShortFundingDataDto of(Funding funding) {
        return ShortFundingDataDto.builder()
                .fundingId(funding.getFunding_id())
                .title(funding.getTitle())
                .mainImage(funding.getMainImage())
                .state(String.valueOf(funding.getCurrent()))
                .build();
    }
}
