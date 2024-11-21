package sg.backend.dto.response.writefunding.project;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class StoryImageResponseDto extends ResponseDto{
    private String filename;

    public StoryImageResponseDto(String filename) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.filename = filename;
    }

    public static ResponseEntity<ResponseDto> not_existed_file(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_FILE, ResponseMessage.NOT_EXISTED_FILE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<StoryImageResponseDto> success(String filename) {
        StoryImageResponseDto result = new StoryImageResponseDto(filename);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
