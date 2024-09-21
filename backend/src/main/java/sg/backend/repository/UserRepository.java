package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  
    Optional<User> findByEmail(String email);
  
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    User findByUserId(Long userId);
}