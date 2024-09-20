package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.MyFundingDataDto;
import sg.backend.dto.response.ResponseDto;

import java.util.List;

@Getter
public class GetMyFundingListResponseDto extends ResponseDto {

    private List<MyFundingDataDto> data;
    private int todayAmount;
    private int todayLikes;

    private GetMyFundingListResponseDto(List<MyFundingDataDto> data, int todayAmount, int todayLikes) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.todayAmount = todayAmount;
        this.todayLikes = todayLikes;
    }

    public static ResponseEntity<GetMyFundingListResponseDto> success(List<MyFundingDataDto> data, int todayAmount, int todayLikes) {
        GetMyFundingListResponseDto result = new GetMyFundingListResponseDto(data, todayAmount, todayLikes);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
