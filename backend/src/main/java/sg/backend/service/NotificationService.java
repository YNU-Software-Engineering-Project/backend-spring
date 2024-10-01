package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.object.NotificationDataDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.notification.GetNotificationsResponseDto;
import sg.backend.entity.Notification;
import sg.backend.entity.User;
import sg.backend.exception.CustomException;
import sg.backend.repository.NotificationRepository;
import sg.backend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static sg.backend.service.UserService.findUserByEmail;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public ResponseEntity<GetNotificationsResponseDto> getNotifications(String email, int page, int size) {

        User user = findUserByEmail(email, userRepository);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notificationList = notificationRepository.findByUser(user, pageRequest);

        List<NotificationDataDto> data = notificationList.stream()
                .map(NotificationDataDto::of)
                .collect(Collectors.toList());

        return GetNotificationsResponseDto.success(notificationList, data);
    }

    public ResponseEntity<ResponseDto> deleteNotifications(String email) {

        User user = findUserByEmail(email, userRepository);
        notificationRepository.deleteByUser(user);

        return ResponseDto.success();
    }

    public ResponseEntity<ResponseDto> deleteNotification(String email, Long notificationId) {

        findUserByEmail(email, userRepository);

        notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_EXISTED_NOTIFICATION, ResponseMessage.NOT_EXISTED_NOTIFICATION));

        notificationRepository.deleteById(notificationId);

        return ResponseDto.success();
    }
}
