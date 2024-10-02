package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FundingStoryRequestDto {

    @Size(max = 20, message = "상품명은 최대 20자까지 입력할 수 있습니다.")
    private String title;

    @Size(max = 100, message = "요약은 최대 100자까지 입력할 수 있습니다.")
    private String summary;
}
