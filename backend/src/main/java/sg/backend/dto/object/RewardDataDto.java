package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardDataDto {
    int no;
    String rewardName;

    public static RewardDataDto of(int no, String reward) {
        return RewardDataDto.builder()
                .no(no)
                .rewardName(reward)
                .build();
    }
}
