package sg.backend.dto.request.wirtefunding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaySuccessRequestDto {
    private String orderId;
    private Long amount;
    private String paymentKey;
}
