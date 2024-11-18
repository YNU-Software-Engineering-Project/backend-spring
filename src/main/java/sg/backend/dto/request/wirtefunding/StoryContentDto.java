package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoryContentDto {
    @NotNull
    private String content;
}
