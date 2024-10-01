package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.Funder;
import sg.backend.entity.User;

import java.util.List;
import java.util.Optional;

import static sg.backend.service.UserService.formatter;

@Getter
@Builder
public class FunderDataDto {

    private String createdAt;
    private String id;
    private String nickname;
    private String email;
    private String address;
    private String phoneNumber;
    private List<String> rewards;

    public static FunderDataDto of(Funder funder, List<String> rewards) {
        String email = funder.getUser().getEmail();
        String id = email.substring(0, email.indexOf("@"));

        String address = Optional.ofNullable(funder.getUser().getRoadAddress())
                .map(road -> road + " " + funder.getUser().getDetailAddress())
                .orElseGet(() -> Optional.ofNullable(funder.getUser().getLandLotAddress())
                        .map(land -> land + " " + funder.getUser().getDetailAddress())
                        .orElse(""));


        return FunderDataDto.builder()
                .createdAt(funder.getCreatedAt().format(formatter))
                .id(id)
                .nickname(Optional.ofNullable(funder.getUser().getNickname()).orElse(""))
                .email(funder.getUser().getEmail())
                .address(address)
                .phoneNumber(funder.getUser().getPhoneNumber())
                .rewards(rewards)
                .build();
    }

}
