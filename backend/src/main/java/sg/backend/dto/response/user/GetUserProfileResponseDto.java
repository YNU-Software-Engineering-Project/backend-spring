package sg.backend.dto.response.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.UserProfileDataDto;
import sg.backend.dto.response.ResponseDto;

@Getter
public class GetUserProfileResponseDto extends ResponseDto {

    private final UserProfileDataDto data;

    private GetUserProfileResponseDto(UserProfileDataDto data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
    }

    public static ResponseEntity<GetUserProfileResponseDto> success(UserProfileDataDto data) {
        GetUserProfileResponseDto result = new GetUserProfileResponseDto(data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
