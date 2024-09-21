package sg.backend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

@Getter
public class UploadInfoFileResponseDto extends ResponseDto {

    private String orginalName;
    private String uuid_name;

    private UploadInfoFileResponseDto(String orginalName, String uuid_name) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.orginalName = orginalName;
        this.uuid_name = uuid_name;
    }

    public static ResponseEntity<UploadInfoFileResponseDto> success(String orginalName, String uuid_name) {
        UploadInfoFileResponseDto result = new UploadInfoFileResponseDto(orginalName, uuid_name);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_file(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_FILE, ResponseMessage.NOT_EXISTED_FILE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
