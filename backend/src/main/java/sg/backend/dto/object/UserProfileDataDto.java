package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.User;

@Schema
@Getter
@Builder
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

    public static UserProfileDataDto of(User user) {
        String email = user.getEmail();
        int index = email.indexOf("@");

        return UserProfileDataDto.builder()
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .id(email.substring(0, index))
                .phoneNumber(user.getPhoneNumber())
                .schoolEmail(user.getSchoolEmail())
                .postalCode(user.getPostalCode())
                .roadAddress(user.getRoadAddress())
                .landLotAddress(user.getLandLotAddress())
                .detailAddress(user.getDetailAddress())
                .build();
    }
}
