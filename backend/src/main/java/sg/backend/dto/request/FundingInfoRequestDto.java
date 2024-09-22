package sg.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FundingInfoRequestDto {

    private String large_category;
    private String small_category;
    private String organizer_name;
    private String organizer_email;
    private String tax_email;
    private String start_date;
    private String end_date;
    private String target_amount;

}
