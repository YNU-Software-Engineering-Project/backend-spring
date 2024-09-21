package sg.backend.service;

import lombok.RequiredArgsConstructor;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.response.auth.LoginResponseDto;
import sg.backend.entity.User;
import sg.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.jwt.TokenProvider;
import sg.backend.dto.response.ResponseDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public ResponseEntity<ResponseDto> signup(SignUpRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            return SignUpResponseDto.duplicateEmail();
        }

        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(ResponseCode.VALUDATION_FAILED, ResponseMessage.VALUDATION_FAILED));
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto.getEmail(), encodedPassword, signupRequestDto.getPhoneNumber());
        userRepository.save(user);

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        return SignUpResponseDto.success(accessToken);
    }

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return LoginResponseDto.failure();
        }

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        return LoginResponseDto.success(accessToken);
    }
}