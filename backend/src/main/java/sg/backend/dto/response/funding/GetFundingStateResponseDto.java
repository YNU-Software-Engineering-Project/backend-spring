package sg.backend.dto.response.funding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingStateDto;
import sg.backend.dto.response.ResponseDto;

public class GetFundingStateResponseDto extends ResponseDto {

    private FundingStateDto data;

    private GetFundingStateResponseDto(FundingStateDto data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
    }

    public static ResponseEntity<GetFundingStateResponseDto> success(FundingStateDto data) {
        GetFundingStateResponseDto result = new GetFundingStateResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSTION, ResponseMessage.NO_PERMISSTION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

}
