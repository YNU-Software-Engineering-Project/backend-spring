package sg.backend.dto.response.writefunding.reward;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetPolicyResponseDto extends ResponseDto {
    private String refund_policy;
    private String reward_info;
    private GetPolicyResponseDto(String refund, String reward) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.refund_policy = refund;
        this.reward_info = reward;
    }
    public static ResponseEntity<GetPolicyResponseDto> success(String refund, String reward){
        GetPolicyResponseDto result = new GetPolicyResponseDto(refund, reward);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
