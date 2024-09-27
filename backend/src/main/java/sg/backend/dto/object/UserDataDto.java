package sg.backend.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataDto {

    private long no;

    private String id;

    private String nickname;

    private String schoolEmail;

    private String address;

    private String phoneNumber;

    private String createdAt;
}
