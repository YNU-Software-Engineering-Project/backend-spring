package sg.backend.dto.response.writefunding.reward;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetMRewardResponseDto extends ResponseDto {
    private String[] amount;
    private String[] reward_name;
    private String[] reward_description;
    private String[] quantity;
    private Long[] reward_id;

    private GetMRewardResponseDto(String[] amount, String[] name, String[] description, String[] quantity, Long[] reward_id) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.amount = amount;
        this.reward_name = name;
        this.reward_description = description;
        this.quantity = quantity;
        this.reward_id = reward_id;
    }

    public static ResponseEntity<GetMRewardResponseDto> success(String[] amount, String[] name, String[] description, String[] quantity, Long[] reward_id){
        GetMRewardResponseDto result = new GetMRewardResponseDto(amount, name, description, quantity,reward_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
