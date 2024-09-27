package sg.backend.dto.object;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FunderDataDto {

    private String createdAt;
    private String id;
    private String nickname;
    private String email;
    private String address;
    private String phoneNumber;
    private List<String> rewards;
}
