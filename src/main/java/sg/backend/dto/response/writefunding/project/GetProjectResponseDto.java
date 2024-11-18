package sg.backend.dto.response.writefunding.project;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetProjectResponseDto extends ResponseDto {

    private String title;

    private String main_url; //파일 보여주기
    private String main_uuid; //파일 이름

    private String[] image_url; //파일 보여주기
    private String[] image_uuid; //파일 이름

    private String summary;
    private String story;

    private GetProjectResponseDto(String title, String main_url,String main_uuid, String[] image_url, String[] image_uuid,String summary, String story){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.title = title;
        this.main_url = main_url;
        this.main_uuid = main_uuid;
        this.image_url = image_url;
        this.image_uuid = image_uuid;
        this.summary = summary;
        this.story = story;
    }

    public static ResponseEntity<GetProjectResponseDto> success(String title, String main_url,String main_uuid, String[] image_url, String[] image_uuid,String summary, String story) {
        GetProjectResponseDto result = new GetProjectResponseDto(title, main_url, main_uuid, image_url, image_uuid, summary, story);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
