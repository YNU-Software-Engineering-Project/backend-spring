package sg.backend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

@Getter
public class GetProjectResponeDto extends ResponseDto{

    private String title;
    private String main_url;
    private String[] image_url;
    private String summary;

    private GetProjectResponeDto(String title, String main_url, String[] image_url, String summary){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.title = title;
        this.main_url = main_url;
        this.image_url = image_url;
        this.summary = summary;
    }

    public static ResponseEntity<GetProjectResponeDto> success(String title, String main_url, String[] image_url, String summary) {
        GetProjectResponeDto result = new GetProjectResponeDto(title, main_url, image_url, summary);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
