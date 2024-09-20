package sg.backend.service;

import org.springframework.http.ResponseEntity;
import sg.backend.dto.response.notification.DeleteNotificationResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationsResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;

public interface NotificationService {
    ResponseEntity<? super GetNotificationsResponseDto> getNotifications(Long userId, int page, int size);
    ResponseEntity<? super DeleteNotificationsResponseDto> deleteNotifications(Long userId);
    ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(Long userId, Long notificationId);

}
