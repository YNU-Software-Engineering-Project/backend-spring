package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.entity.Notification;
import sg.backend.entity.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);

    @Transactional
    void deleteByUser(User user);
}
