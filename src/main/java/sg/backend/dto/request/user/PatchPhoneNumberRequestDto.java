package sg.backend.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchPhoneNumberRequestDto {

    @NotBlank(message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
    @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$")
    @Schema(
            description = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다."
    )
    private String phoneNumber;
}
