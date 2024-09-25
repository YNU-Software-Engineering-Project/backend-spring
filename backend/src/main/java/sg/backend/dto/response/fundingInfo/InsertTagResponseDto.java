package sg.backend.dto.response.fundingInfo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class InsertTagResponseDto extends ResponseDto {
    private Long tag_id;

    private InsertTagResponseDto(Long tag_id){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.tag_id = tag_id;
    }

    public static ResponseEntity<InsertTagResponseDto> success(Long tag_id){
        InsertTagResponseDto result = new InsertTagResponseDto(tag_id);
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

    public static ResponseEntity<ResponseDto> full_data(){
        ResponseDto result = new ResponseDto(ResponseCode.FULL_DATA, ResponseMessage.FULL_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
