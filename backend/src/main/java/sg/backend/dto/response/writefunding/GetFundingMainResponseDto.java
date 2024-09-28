package sg.backend.dto.response.writefunding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetFundingMainResponseDto extends ResponseDto {
    private String main_url;
    private String funding_title;

    private GetFundingMainResponseDto(String url, String title) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.main_url = url;
        this.funding_title = title;
    }

    public static ResponseEntity<GetFundingMainResponseDto> success(String url, String title){
        GetFundingMainResponseDto result = new GetFundingMainResponseDto(url, title);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
