package sg.backend.dto.response.writefunding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class SubmitFundingResponseDto extends ResponseDto {

    private SubmitFundingResponseDto(){super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);}

    public static ResponseEntity<ResponseDto> not_existed_info(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_INFO, ResponseMessage.NOT_EXISTED_INFO);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_story(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_STORY, ResponseMessage.NOT_EXISTED_STORY);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_reward(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_REWARD, ResponseMessage.NOT_EXISTED_REWARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_policy(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_POLICY, ResponseMessage.NOT_EXISTED_POLICY);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
