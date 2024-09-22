package sg.backend.dto.response.file;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class DeleteFileResponseDto extends ResponseDto {

    public DeleteFileResponseDto(){ super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS); }

    public static ResponseEntity<DeleteFileResponseDto> success(){
        DeleteFileResponseDto result = new DeleteFileResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_file(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_FILE, ResponseMessage.NOT_EXISTED_FILE);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
