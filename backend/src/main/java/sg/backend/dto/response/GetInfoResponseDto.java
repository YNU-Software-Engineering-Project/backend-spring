package sg.backend.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

@Getter
public class GetInfoResponseDto extends ResponseDto {

    private String large_category;
    private String small_category;
    private String organizer_name;
    private String organizer_email;
    private String tax_email;
    private String start_date;
    private String end_date;
    private String target_amount;


    private GetInfoResponseDto(String large, String small, String name, String email, String tax, String start, String end, String amount) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.large_category = large;
        this.small_category = small;
        this.organizer_name = name;
        this.organizer_email = email;
        this.tax_email = tax;
        this.start_date = start;
        this.end_date = end;
        this.target_amount = amount;
    }

    public static ResponseEntity<GetInfoResponseDto> success(String large, String small, String name, String email, String tax, String start, String end, String amount) {
        GetInfoResponseDto result = new GetInfoResponseDto(large,small, name, email, tax, start, end, amount);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> not_existed_post(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
