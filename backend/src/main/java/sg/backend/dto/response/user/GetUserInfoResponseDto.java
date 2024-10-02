package sg.backend.dto.response.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.UserInfoDataDto;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetUserInfoResponseDto extends ResponseDto {

    private final UserInfoDataDto data;

    private GetUserInfoResponseDto(UserInfoDataDto data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
    }

    public static ResponseEntity<GetUserInfoResponseDto> success(UserInfoDataDto data) {
        GetUserInfoResponseDto result = new GetUserInfoResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
