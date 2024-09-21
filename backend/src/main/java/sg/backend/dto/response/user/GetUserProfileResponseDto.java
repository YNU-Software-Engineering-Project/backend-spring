package sg.backend.dto.response.user;

import lombok.Getter;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.UserProfileDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.User;

@Getter
public class GetUserProfileResponseDto extends ResponseDto {

    private UserProfileDataDto data;

    private GetUserProfileResponseDto(User user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        UserProfileDataDto data = new UserProfileDataDto();

        String email = user.getEmail();
        int index = email.indexOf("@");

        data.setProfileImage(user.getProfileImage());
        data.setNickname(user.getNickname());
        data.setId(email.substring(0, index));
        data.setPhoneNumber(user.getPhoneNumber());
        data.setSchoolEmail(user.getSchoolEmail());
        data.setPostalCode(user.getPostalCode());
        data.setRoadAddress(user.getRoadAddress());
        data.setLandLotAddress(user.getLandLotAddress());
        data.setDetailAddress(user.getDetailAddress());

        this.data = data;
    }

    public static ResponseEntity<GetUserProfileResponseDto> success(User user) {
        GetUserProfileResponseDto result = new GetUserProfileResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
