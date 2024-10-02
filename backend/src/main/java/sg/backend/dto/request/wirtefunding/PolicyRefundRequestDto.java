package sg.backend.dto.request.wirtefunding;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PolicyRefundRequestDto {

    @Size(max = 1000, message = "환불 정책은 최대 1000자까지 입력할 수 있습니다.")
    private String refund_policy;
}
