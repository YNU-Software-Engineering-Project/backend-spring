package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakeRewardRequestDto {
    private String amount;

    @Size(max = 30, message = "리워드명은 최대 30자까지 입력할 수 있습니다.")
    private String reward_name;

    @Size(max = 300, message = "설명은 최대 300자까지 입력할 수 있습니다.")
    private String reward_description;

    private String quantity;
}
