package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.FundingDataDto;
import sg.backend.dto.response.ResponseDto;

import java.util.List;

@Getter
public class GetUserWishListResponseDto extends ResponseDto {

    private List<FundingDataDto> data;

    private GetUserWishListResponseDto(List<FundingDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
    }

    public static ResponseEntity<GetUserWishListResponseDto> success(List<FundingDataDto> data) {
        GetUserWishListResponseDto result = new GetUserWishListResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
