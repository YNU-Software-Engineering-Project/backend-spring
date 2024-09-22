package sg.backend.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.backend.dto.request.email.EmailSendTokenRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.email.EmailSendTokenResponseDto;
import sg.backend.dto.response.user.EmailVerificationResponseDto;
import sg.backend.dto.response.user.GetUserProfileResponseDto;
import sg.backend.entity.EmailToken;
import sg.backend.entity.User;
import sg.backend.repository.EmailTokenRepository;
import sg.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;

    @Async
    public void sendEmail(MimeMessage email) {
        javaMailSender.send(email);
    }

    public ResponseEntity<? super EmailSendTokenResponseDto> createEmailToken(EmailSendTokenRequestDto dto, String email) {

        User user;

        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if(optionalUser.isEmpty()) return EmailSendTokenResponseDto.noExistUser();
            user = optionalUser.get();

            String receiverEmail = dto.getEmail();
            int index = receiverEmail.indexOf("@");
            String domain = receiverEmail.substring(index+1);
            if(!domain.contains("ac.kr")) return EmailSendTokenResponseDto.validationFailed();

            EmailToken emailToken = EmailToken.createEmailToken(user.getUserId());
            emailToken.setEmail(receiverEmail);
            emailTokenRepository.save(emailToken);

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

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailSendTokenResponseDto.success();
    }

    public EmailToken findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailTokenId) {
        Optional<EmailToken> emailToken = emailTokenRepository
                .findByEmailTokenIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false);

        return emailToken.orElse(null);
    }

    @Transactional
    public ResponseEntity<? super EmailVerificationResponseDto> verifyEmail(String token) {

        User user;

        try {
            EmailToken emailToken = findByEmailTokenIdAndExpirationDateAfterAndExpired(token);
            if(emailToken == null) return EmailVerificationResponseDto.invalidToken();

            user = userRepository.findByUserId(emailToken.getUserId());
            emailToken.setTokenToUsed();

            if(user == null) return EmailVerificationResponseDto.noExistUser();

            user.setSchoolEmail(emailToken.getEmail());
            user.setSchoolEmailVerified(true);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailVerificationResponseDto.success();
    }
}
