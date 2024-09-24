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

    private FundingStateDto data;

    private GetFundingStateCountResponseDto(long review, long reviewCompleted, long onGoing) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = new FundingStateDto();
        this.data.setReview(review);
        this.data.setReviewCompleted(reviewCompleted);
        this.data.setOnGoing(onGoing);
    }

    public static ResponseEntity<GetFundingStateCountResponseDto> success(long review, long reviewCompleted, long onGoing) {
        GetFundingStateCountResponseDto result = new GetFundingStateCountResponseDto(review, reviewCompleted, onGoing);
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
