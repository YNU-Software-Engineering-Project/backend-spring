package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.User;

import java.util.Optional;

import static sg.backend.service.UserService.formatter;

@Getter
@Builder
public class UserDataDto {

    private int no;
    private Long userId;
    private String id;
    private String nickname;
    private String schoolEmail;
    private String address;
    private String phoneNumber;
    private String createdAt;

    public static UserDataDto of(User user) {
        String email = user.getEmail();
        String id = email.substring(0, email.indexOf("@"));

        String address = Optional.ofNullable(user.getRoadAddress())
                .map(road -> road + " " + user.getDetailAddress())
                .orElseGet(() -> Optional.ofNullable(user.getLandLotAddress())
                        .map(land -> land + " " + user.getDetailAddress())
                        .orElse(""));

        return UserDataDto.builder()
                .userId(user.getUserId())
                .id(id)
                .nickname(Optional.ofNullable(user.getNickname()).orElse(""))
                .schoolEmail(user.getSchoolEmail())
                .address(address)
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt().format(formatter))
                .build();
    }

    public void setNo(int no) {
        this.no = no;
    }
}
