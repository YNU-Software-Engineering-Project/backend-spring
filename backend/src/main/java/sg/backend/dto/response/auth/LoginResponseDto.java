package sg.backend.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Role;

@Getter
public class LoginResponseDto extends ResponseDto {

    private final String accessToken;
    private final Role role;

    private LoginResponseDto(String accessToken, Role role) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.accessToken = accessToken;
        this.role = role;
    }

    public static ResponseEntity<LoginResponseDto> success(String accessToken, Role role) {
        LoginResponseDto result = new LoginResponseDto(accessToken, role);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> failure() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
