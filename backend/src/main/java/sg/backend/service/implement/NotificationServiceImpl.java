package sg.backend.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.notification.DeleteNotificationsResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.entity.Notification;
import sg.backend.entity.User;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;
import sg.backend.service.NotificationService;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<? super GetNotificationsResponseDto> getNotifications(Long userId, int page, int size) {

        User user = null;
        Page<Notification> notificationList;

        try {
            userId = 1L;
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

    @Override
    public ResponseEntity<? super DeleteNotificationsResponseDto> deleteNotifications(Long userId) {

        User user = null;

        try {
            userId = 1L;
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
