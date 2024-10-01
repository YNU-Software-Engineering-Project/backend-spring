package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPhoneNumber(String email,String phoneNumber);
  
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByUserId(Long userId);

    long countByCreatedAtAfter(LocalDateTime dateTime);
}