package sg.backend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

@Getter
public class MakeRewardResponseDto extends ResponseDto{
    private Long reward_id;

    private MakeRewardResponseDto(Long reward_id) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.reward_id = reward_id;
    }

    public static ResponseEntity<MakeRewardResponseDto> success(Long reward_id){
        MakeRewardResponseDto result = new MakeRewardResponseDto(reward_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_data(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATA, ResponseMessage.NOT_EXISTED_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
