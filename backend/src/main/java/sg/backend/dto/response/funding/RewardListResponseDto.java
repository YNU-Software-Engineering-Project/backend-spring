package sg.backend.dto.response.funding;

import lombok.Getter;
import lombok.Setter;
import sg.backend.entity.Reward;

@Getter
@Setter
public class RewardListResponseDto {
    private Long rewardId;
    private Integer amount;
    private String rewardName;
    private String rewardDescription;
    private Integer quantity;

    public RewardListResponseDto(Reward reward) {
        this.rewardId = reward.getRewardId();
        this.amount = reward.getAmount();
        this.rewardName = reward.getRewardName();
        this.rewardDescription = reward.getRewardDescription();
        this.quantity = reward.getQuantity();
    }
}
