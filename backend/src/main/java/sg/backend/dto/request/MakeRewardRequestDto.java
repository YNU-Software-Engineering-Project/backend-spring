package sg.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakeRewardRequestDto {
    private String amount;

    private String reward_name;

    private String reward_description;

    private String quantity;
}
