package sg.backend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

@Getter
public class UploadImageResponseDto extends ResponseDto{

    private String url;
    private String uuid_name;

    private UploadImageResponseDto(String uuid_name, String url){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.uuid_name = uuid_name;
        this.url = url;
    }

    public static ResponseEntity<UploadImageResponseDto> success(String uuid_name, String url) {
        UploadImageResponseDto result = new UploadImageResponseDto(uuid_name, url);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> unsupported_file() {
        ResponseDto result = new ResponseDto(ResponseCode.UNSUPPORTED_MEDIA_TYPE, ResponseMessage.UNSUPPORTED_MEDIA_TYPE);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_file(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_FILE, ResponseMessage.NOT_EXISTED_FILE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
