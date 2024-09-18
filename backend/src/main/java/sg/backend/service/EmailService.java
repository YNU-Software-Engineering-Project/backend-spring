package sg.backend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import sg.backend.dto.request.email.EmailSendTokenRequestDto;
import sg.backend.dto.response.email.EmailSendTokenResponseDto;
import sg.backend.dto.response.user.EmailVerificationResponseDto;
import sg.backend.entity.EmailToken;

public interface EmailService {
    void sendEmail(MimeMessage email);
    ResponseEntity<? super EmailSendTokenResponseDto> createEmailToken(EmailSendTokenRequestDto dto, Long userId);
    EmailToken findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailTokenId);
    ResponseEntity<? super EmailVerificationResponseDto> verifyEmail(String token);

}
