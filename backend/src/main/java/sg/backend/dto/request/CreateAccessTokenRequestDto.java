package sg.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateAccessTokenRequestDto {
    private String refreshToken;
}
