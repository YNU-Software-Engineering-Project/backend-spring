package sg.backend.dto.response.notification;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.NotificationDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Notification;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetNotificationsResponseDto extends ResponseDto {

    private List<NotificationDataDto> data;

    private GetNotificationsResponseDto(List<Notification> notificationList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        List<NotificationDataDto> data = new ArrayList<>();

        for(Notification n : notificationList) {
            data.add(new NotificationDataDto(n.getMessage(), n.getCreatedAt()));
        }

        this.data = data;
    }

    public static ResponseEntity<GetNotificationsResponseDto> success(List<Notification> notificationList) {
        GetNotificationsResponseDto result = new GetNotificationsResponseDto(notificationList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}

