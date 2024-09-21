package sg.backend.dto.object;

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
    private String postalCode;
    private String roadAddress;
    private String landLotAddress;
    private String detailAddress;
}
