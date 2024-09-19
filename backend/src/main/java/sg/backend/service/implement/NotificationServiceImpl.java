package sg.backend.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationsResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.entity.Notification;
import sg.backend.entity.User;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;
import sg.backend.service.NotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(Long userId) {

        User user = null;
        List<Notification> notificationList;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetNotificationsResponseDto.noExistUser();

            notificationList = notificationRepository.findByUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetNotificationsResponseDto.success(notificationList);
    }

    @Override
    public ResponseEntity<? super DeleteNotificationsResponseDto> deleteNotifications(Long userId) {

        User user = null;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return DeleteNotificationsResponseDto.noExistUser();

            notificationRepository.deleteByUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteNotificationsResponseDto.success();
    }
}
