package sg.backend.dto.response.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Role;

@Getter
public class SignUpResponseDto extends ResponseDto {

    private final String accessToken;
    private final Role role;

    private SignUpResponseDto(String accessToken, Role role){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.accessToken = accessToken;
        this.role = role;
    }

    public static ResponseEntity<ResponseDto> success(String accessToken, Role role){
        SignUpResponseDto result = new SignUpResponseDto(accessToken,role);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicateEmail(){
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}

