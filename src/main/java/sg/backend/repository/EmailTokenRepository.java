package sg.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.backend.entity.EmailToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
    Optional<EmailToken> findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailTokenId, LocalDateTime now, boolean expired);
}