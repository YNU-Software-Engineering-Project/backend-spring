package sg.backend.dto.response.writefunding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class RegisterResponseDto extends ResponseDto {

    private Long funding_id;

    private RegisterResponseDto(Long funding_id) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.funding_id = funding_id;
    }

    public static ResponseEntity<RegisterResponseDto> success(Long funding_id){
        RegisterResponseDto result = new RegisterResponseDto(funding_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_schoolemail(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_SCHOOLEMAIL, ResponseMessage.NOT_EXISTED_SCHOOLEMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
