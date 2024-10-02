package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InsertTagRequestDto {

    @Size(max = 10, message = "내용은 최대 10자까지 입력할 수 있습니다.")
    private String tagName;
}
