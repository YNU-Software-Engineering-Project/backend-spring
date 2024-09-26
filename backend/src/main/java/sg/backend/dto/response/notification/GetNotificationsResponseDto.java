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

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetNotificationsResponseDto extends ResponseDto {

    private List<NotificationDataDto> data;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    private GetNotificationsResponseDto(Page<Notification> notificationList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        List<NotificationDataDto> data = new ArrayList<>();

        for(Notification n : notificationList) {
            data.add(new NotificationDataDto(n.getMessage(), n.getCreatedAt()));
        }

        this.data = data;
        this.page = notificationList.getNumber();
        this.size = notificationList.getSize();
        this.totalPages = notificationList.getTotalPages();
        this.totalElements = notificationList.getTotalElements();
    }

    public static ResponseEntity<GetNotificationsResponseDto> success(Page<Notification> notificationList) {
        GetNotificationsResponseDto result = new GetNotificationsResponseDto(notificationList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}

