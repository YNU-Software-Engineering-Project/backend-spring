package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.Role;
import sg.backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPhoneNumber(String email,String phoneNumber);
    Optional<User> findBySocialIdAndSocialProvider(String socialId, String socialProvider);
  
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByUserId(Long userId);

    long countByCreatedAtAfter(LocalDateTime dateTime);
    List<User> findByRole(Role role);
}