package sg.backend.dto.object;

import lombok.Builder;
import lombok.Getter;
import sg.backend.entity.Notification;

@Getter
@Builder
public class NotificationDataDto {
    private Long notificationId;
    private String message;
    private String createdAt;

    public static NotificationDataDto of(Notification notification) {
        return NotificationDataDto.builder()
                .notificationId(notification.getNotification_id())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
