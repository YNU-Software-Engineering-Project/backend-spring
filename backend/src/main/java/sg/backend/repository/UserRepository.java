package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}