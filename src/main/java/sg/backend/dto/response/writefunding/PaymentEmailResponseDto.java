package sg.backend.dto.response.writefunding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

public class PaymentEmailResponseDto extends ResponseDto {

    private PaymentEmailResponseDto(){super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);}

    public static ResponseEntity<ResponseDto> not_existed_data(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATA, ResponseMessage.NOT_EXISTED_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
