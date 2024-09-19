package sg.backend.service;

import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.entity.User;
import sg.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;
import sg.backend.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;



@Service
public class UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public ApiResponse signup(SignupRequestDto signupRequestDto){
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL));
        }

        if (!signupRequestDto.getPassword().equals(signupRequest.getPasswordConfirm())){
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(ResponseCode.VALUDATION_FAILED, ResponseMessage.VALUDATION_FAILED));
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        User user = new User(signupRequest.getEmail(), encodedPassword, signupRequest.getPhoneNumber());
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }
}