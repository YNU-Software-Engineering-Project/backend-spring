package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.Role;
import sg.backend.entity.User;

@Getter
@Builder
public class UserInfoDataDto {

    private Long user_id;
    private String id;
    private String nickname;
    private String schoolEmail;
    private String phoneNumber;

    @Schema(description = "우편번호")
    private String postalCode;

    @Schema(description = "도로명 주소")
    private String roadAddress;

    @Schema(description = "지번 주소")
    private String landLotAddress;

    @Schema(description = "상세 주소")
    private String detailAddress;

    private Role role;

    public static UserInfoDataDto of(User user) {
        String email = user.getEmail();
        int index = email.indexOf("@");

        return UserInfoDataDto.builder()
                .user_id(user.getUserId())
                .id(email.substring(0, index))
                .nickname(user.getNickname())
                .schoolEmail(user.getSchoolEmail())
                .phoneNumber(user.getPhoneNumber())
                .postalCode(user.getPostalCode())
                .roadAddress(user.getRoadAddress())
                .landLotAddress(user.getLandLotAddress())
                .detailAddress(user.getDetailAddress())
                .role(user.getRole())
                .build();
    }
}
