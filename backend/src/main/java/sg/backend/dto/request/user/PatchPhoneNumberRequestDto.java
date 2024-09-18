package sg.backend.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchPhoneNumberRequestDto {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11,13}$")
    private String phoneNumber;
}
