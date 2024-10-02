package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingStateDto;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetFundingStateCountResponseDto extends ResponseDto {

    private final FundingStateDto data;

    private GetFundingStateCountResponseDto(FundingStateDto data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
    }

    public static ResponseEntity<GetFundingStateCountResponseDto> success(FundingStateDto data) {
        GetFundingStateCountResponseDto result = new GetFundingStateCountResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
