package sg.backend.dto.response.email;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class EmailSendTokenResponseDto extends ResponseDto {

    private EmailSendTokenResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<EmailSendTokenResponseDto> success() {
        EmailSendTokenResponseDto result = new EmailSendTokenResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> invalidUniversityEmail() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_UNIVERSITY_EMAIL, ResponseMessage.INVALID_UNIVERSITY_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
