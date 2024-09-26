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

    @Email
    private String organizer_email;

    @Email
    private String tax_email;

    private String start_date;
    private String end_date;
    private String target_amount;

}
