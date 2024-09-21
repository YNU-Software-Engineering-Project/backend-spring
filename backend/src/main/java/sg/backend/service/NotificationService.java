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
import sg.backend.dto.response.user.GetUserProfileResponseDto;
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

    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(String email, int page, int size) {

        User user;
        Page<Notification> notificationList;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            notificationList = notificationRepository.findByUser(user, pageRequest);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetNotificationsResponseDto.success(notificationList);
    }

    public ResponseEntity<? super DeleteNotificationsResponseDto> deleteNotifications(String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

            notificationRepository.deleteByUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return DeleteNotificationsResponseDto.success();
    }

    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(String email, Long notificationId) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return GetUserProfileResponseDto.noExistUser();
            user = optionalUser.get();

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
