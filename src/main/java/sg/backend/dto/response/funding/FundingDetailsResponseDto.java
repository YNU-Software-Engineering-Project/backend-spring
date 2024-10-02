package sg.backend.dto.response.funding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundingDetailsResponseDto {

    private String rewardInfo;
    private String refundPolicy;
    private Integer targetAmount;
    private Integer CurrentAmount;
    private Double achievementRate;
    private Integer remainingDays;
    private Long supporterCount;
    private String story;
}