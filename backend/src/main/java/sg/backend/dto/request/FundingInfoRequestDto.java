package sg.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sg.backend.entity.Category;
import sg.backend.entity.SubCategory;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class FundingInfoRequestDto {

    private Category large_category;
    private SubCategory small_category;
    private String organizer_name;
    private String organizer_email;
    private String tax_email;
    private LocalDate start_date;
    private LocalDate end_date;
    private Integer target_amount;

}
