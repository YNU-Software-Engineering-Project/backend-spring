package sg.backend.dto.response.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class EmailVerificationResponseDto extends ResponseDto {

    private EmailVerificationResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<ResponseDto> invalidToken() {
        ResponseDto result = new ResponseDto(ResponseCode.EMAIL_TOKEN_NOT_FOUND, ResponseMessage.EMAIL_TOKEN_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
