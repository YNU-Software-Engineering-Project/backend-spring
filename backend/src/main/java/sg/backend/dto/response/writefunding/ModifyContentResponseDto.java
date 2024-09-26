package sg.backend.dto.response.writefunding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class ModifyContentResponseDto extends ResponseDto {

    private ModifyContentResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<ModifyContentResponseDto> success() {
        ModifyContentResponseDto result = new ModifyContentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_permission(){
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSTION, ResponseMessage.NO_PERMISSTION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
