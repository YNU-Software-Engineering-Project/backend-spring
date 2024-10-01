package sg.backend.dto.response.notification;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.NotificationDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.entity.Notification;

import java.util.List;

@Getter
public class GetNotificationsResponseDto extends ResponseDto {

    private final List<NotificationDataDto> data;
    private final int page;
    private final int size;
    private final int totalPages;
    private final long totalElements;

    private GetNotificationsResponseDto(Page<Notification> notificationList, List<NotificationDataDto> data) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.data = data;
        this.page = notificationList.getNumber();
        this.size = notificationList.getSize();
        this.totalPages = notificationList.getTotalPages();
        this.totalElements = notificationList.getTotalElements();
    }

    public static ResponseEntity<GetNotificationsResponseDto> success(Page<Notification> notificationList, List<NotificationDataDto> data) {
        GetNotificationsResponseDto result = new GetNotificationsResponseDto(notificationList, data);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

