package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDataDto {

    private String profileImage;
    private String nickname;
    private String id;
    private String phoneNumber;
    private String schoolEmail;

    @Schema(description = "우편번호")
    private String postalCode;

    @Schema(description = "도로명 주소")
    private String roadAddress;

    @Schema(description = "지번 주소")
    private String landLotAddress;

    @Schema(description = "상세 주소")
    private String detailAddress;
}
