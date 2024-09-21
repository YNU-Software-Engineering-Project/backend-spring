package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationsResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.entity.Notification;
import sg.backend.entity.User;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(Long userId, int page, int size) {

        User user = null;
        Page<Notification> notificationList;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return GetNotificationsResponseDto.noExistUser();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            notificationList = notificationRepository.findByUser(user, pageRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetNotificationsResponseDto.success(notificationList);
    }

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

    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(Long userId, Long notificationId) {

        User user = null;

        try {
            user = userRepository.findByUserId(userId);
            if(user == null) return DeleteNotificationResponseDto.noExistUser();

            Optional<Notification> notification = notificationRepository.findById(notificationId);
            if(!notification.isPresent())
                return DeleteNotificationResponseDto.noExistNotification();

            notificationRepository.deleteById(notificationId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteNotificationResponseDto.success();
    }
}