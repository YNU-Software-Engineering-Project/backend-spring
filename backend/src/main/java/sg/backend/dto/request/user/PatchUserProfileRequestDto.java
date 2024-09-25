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
public class PatchUserProfileRequestDto {

    private String nickname;

    private String password;

    private String confirmPassword;

    @Schema(description = "우편번호")
    private String postalCode;

    @Schema(description = "도로명 주소")
    private String roadAddress;

    @Schema(description = "지번 주소")
    private String landLotAddress;

    @Schema(description = "상세 주소")
    private String detailAddress;

}
