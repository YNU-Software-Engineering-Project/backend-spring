package sg.backend.controller;
import sg.backend.dto.request.auth.LoginRequestDto;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController{

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return userService.signup(signUpRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }
}