package sg.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakeRewardRequestDto {
    @NotBlank
    private String amount;
    @NotBlank
    private String reward_name;
    @NotBlank
    private String reward_description;
    @NotBlank
    private String quantity;
}
