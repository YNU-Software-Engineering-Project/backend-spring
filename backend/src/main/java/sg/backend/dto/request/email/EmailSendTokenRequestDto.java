package sg.backend.dto.request.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailSendTokenRequestDto {

    @NotBlank(message = "이메일 주소는 필수 입력값입니다.")
    @Email
    private String email;
}