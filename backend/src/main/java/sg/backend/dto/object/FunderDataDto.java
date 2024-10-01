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

}
