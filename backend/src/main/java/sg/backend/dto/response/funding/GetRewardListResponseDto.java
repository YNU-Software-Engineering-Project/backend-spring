package sg.backend.dto.response.funding;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.RewardDataDto;
import sg.backend.dto.response.ResponseDto;

import java.util.List;

@Getter
public class GetRewardListResponseDto extends ResponseDto {

    private List<RewardDataDto> data;
    private int totalElements;

    private GetRewardListResponseDto(List<RewardDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.totalElements = data.size();
    }

    public static ResponseEntity<GetRewardListResponseDto> success(List<RewardDataDto> data) {
        GetRewardListResponseDto result = new GetRewardListResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
