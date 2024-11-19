package sg.backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.backend.jwt.CustomPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/funding")
@RestController
public class PaymentController {

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@AuthenticationPrincipal CustomPrincipal principal) {
        String provider = principal.getProvider(); // provider 값 가져오기

        if ("kakao".equalsIgnoreCase(provider)) {
            return ResponseEntity.ok("Kakao 사용자 처리");
        }
        if ("google".equalsIgnoreCase(provider)) {
            return ResponseEntity.ok("Google 사용자 처리");
        }
        if ("naver".equalsIgnoreCase(provider)) {
            return ResponseEntity.ok("Naver 사용자 처리");
        }

        return ResponseEntity.badRequest().body("알 수 없는 provider");
    }
}