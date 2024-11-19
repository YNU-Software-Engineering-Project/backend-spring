package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FundingInfoRequestDto {

    private String category;
    private String organizer_name;

    @Email(message = "대표자 이메일 형식이 맞지 않습니다.")
    private String organizer_email;

    @Email(message = "세금계산서 발행 이메일 형식이 맞지 않습니다.")
    private String tax_email;

    private String start_date;
    private String end_date;
    private String target_amount;

}
