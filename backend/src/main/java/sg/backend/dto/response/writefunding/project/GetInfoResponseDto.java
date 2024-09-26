package sg.backend.dto.response.writefunding.project;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetInfoResponseDto extends ResponseDto {

    private String category;

    private String[] tag;  //태그 이름들을 반환
    private Long[] tag_id;  //태그를 지우기위해 필요

    private String idCard_url; //파일 다운로드
    private String idCard_uuid; //파일 이름

    private String organizer_name;
    private String organizer_email;
    private String tax_email;

    private String[] document_url; //파일 다운로드
    private String[] document_name; //파일 이름
    private String[] document_uuid; //파일 지우기 위해

    private String start_date;
    private String end_date;
    private String target_amount;


    private GetInfoResponseDto(String category, String[] tag, Long[] tag_id, String idCard_url,
                               String idCard_uuid, String name, String email, String tax,
                               String[] document_url, String[] document_name,String[] document_uuid,
                               String start, String end, String amount) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.category = category;

        this.tag = tag;
        this.tag_id = tag_id;

        this.idCard_url = idCard_url;
        this.idCard_uuid = idCard_uuid;

        this.organizer_name = name;
        this.organizer_email = email;
        this.tax_email = tax;

        this.document_url = document_url;
        this.document_name = document_name;
        this.document_uuid = document_uuid;

        this.start_date = start;
        this.end_date = end;
        this.target_amount = amount;
    }

    public static ResponseEntity<GetInfoResponseDto> success(String category, String[] tag, Long[] tag_id, String idCard_url,
                                                             String idCard_uuid, String name, String email, String tax,
                                                             String[] document_url, String[] document_name,String[] document_uuid,
                                                             String start, String end, String amount) {
        GetInfoResponseDto result = new GetInfoResponseDto(category,tag,tag_id,idCard_url,idCard_uuid,
                name,email,tax,document_url,document_name,document_uuid,start,end,amount);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
