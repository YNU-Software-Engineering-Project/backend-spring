package sg.backend.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class LoginResponseDto extends ResponseDto {
    private String accessToken;

    private LoginResponseDto(String accessToken) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.accessToken = accessToken;
    }

    public static ResponseEntity<LoginResponseDto> success(String accessToken) {
        LoginResponseDto result = new LoginResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> failure() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
