package sg.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.request.email.EmailSendTokenRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.user.EmailVerificationResponseDto;
import sg.backend.entity.EmailToken;
import sg.backend.entity.User;
import sg.backend.exception.CustomException;
import sg.backend.repository.EmailTokenRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;

import static sg.backend.service.UserService.findUserByEmail;
import static sg.backend.service.UserService.findUserById;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;

    @Async
    public void sendEmail(String receiverEmail, EmailToken emailToken) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(receiverEmail);
        helper.setSubject("SparkSeed 대학교 메일 인증");

        String body = "<div>"
                + "<h1> 안녕하세요. SparkSeed 입니다</h1>"
                + "<br>"
                + "<p>아래 링크를 클릭하면 대학교 메일 인증이 완료됩니다.<p>"
                + "<a href='http://localhost:8080/api/confirm-email?token=" + emailToken.getEmailTokenId() + "'>인증 링크</a>"
                + "</div>";

        helper.setText(body, true);

        javaMailSender.send(message);
    }

    public ResponseEntity<ResponseDto> createEmailToken(EmailSendTokenRequestDto dto, String email) {

        User user = findUserByEmail(email, userRepository);
        String receiverEmail = dto.getEmail();

        if(!isValidUniversityEmail(receiverEmail))
            throw new CustomException(ResponseCode.VALIDATION_FAILED, "This is not a valid university email address.");

        EmailToken emailToken = EmailToken.createEmailToken(user.getUserId());
        emailToken.setEmail(receiverEmail);
        emailTokenRepository.save(emailToken);

        try {
            sendEmail(receiverEmail, emailToken);
        } catch (MessagingException e) {
            throw new CustomException("EMAIL_SENDING_FAILED", e.getMessage());
        }

        return ResponseDto.success();
    }

    private boolean isValidUniversityEmail(String receiverEmail) {
        int index = receiverEmail.indexOf("@");
        String domain = receiverEmail.substring(index+1);
        return domain.endsWith("ac.kr");
    }

    public EmailToken findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailTokenId) {
        return emailTokenRepository
                .findByEmailTokenIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false)
                .orElseThrow(() -> new CustomException(ResponseCode.EMAIL_TOKEN_NOT_FOUND, ResponseMessage.EMAIL_TOKEN_NOT_FOUND));
    }

    @Transactional
    public ResponseEntity<? super EmailVerificationResponseDto> verifyEmail(String token) {

        EmailToken emailToken = findByEmailTokenIdAndExpirationDateAfterAndExpired(token);

        User user = findUserById(emailToken.getUserId(), userRepository);
        emailToken.setTokenToUsed();

        user.setSchoolEmail(emailToken.getEmail());
        user.setSchoolEmailVerified(true);

        return EmailVerificationResponseDto.success();
    }
}
