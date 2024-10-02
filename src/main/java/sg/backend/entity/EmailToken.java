package sg.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailToken {
    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;    // 이메일 토큰 만료 시간

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String emailTokenId;

    private LocalDateTime expirationDate;

    private boolean expired;

    private Long userId;

    private String email;


    // 이메일 인증 토큰 생성
    public static EmailToken createEmailToken(Long userId) {
        EmailToken emailToken = new EmailToken();
        emailToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        emailToken.expired = false;
        emailToken.userId = userId;

        return emailToken;
    }

    // 토큰 만료
    public void setTokenToUsed() {
        this.expired = true;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
