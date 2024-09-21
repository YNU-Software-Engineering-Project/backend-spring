package sg.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FundingStoryRequestDto {
    private String title;
    private String summary;
}
