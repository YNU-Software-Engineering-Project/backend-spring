package sg.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.backend.dto.request.auth.SignUpRequestDto;
import sg.backend.dto.response.auth.SignUpResponseDto;
import sg.backend.service.AuthService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("")
    public ResponseEntity<? super SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }
}
