package sg.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.auth.PasswordResetRequestDto;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.PasswordResetResponseDto;
import sg.backend.entity.User;
import sg.backend.repository.UserRepository;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    public ResponseEntity<ResponseDto> resetPassword(PasswordResetRequestDto passwordResetRequestDto){
        Optional<User> userOpt;
        userOpt = userRepository.findByEmailAndPhoneNumber(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getPhoneNumber());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String newPassword = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            sendEmail(user.getEmail(), newPassword);
            return ResponseDto.success();
        } else {
            return PasswordResetResponseDto.noExistUser();
        }
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        while (password.length() < 12) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    public void sendEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("비밀번호가 초기화되었습니다. 이메일을 확인하세요.");
        message.setText("초기화된 임시 비밀번호 :  " + newPassword);
        emailSender.send(message);
    }
}
