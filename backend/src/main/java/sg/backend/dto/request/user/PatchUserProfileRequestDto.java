package sg.backend.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchUserProfileRequestDto {

    @NotBlank
    private String nickname;

    private String password;

    private String confirmPassword;

    private String postalCode;

    private String roadAddress;

    private String landLotAddress;

    private String detailAddress;

}
