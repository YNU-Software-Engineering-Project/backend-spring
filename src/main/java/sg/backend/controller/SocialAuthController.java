package sg.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Qualifier;
import sg.backend.dto.response.auth.LoginResponseDto;
import sg.backend.entity.User;
import sg.backend.jwt.TokenProvider;
import sg.backend.repository.UserRepository;
import sg.backend.service.socialLogin.SocialLoginService;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class SocialAuthController {

    private final SocialLoginService kakaoLoginService;
    private final SocialLoginService googleLoginService;
    private final SocialLoginService naverLoginService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    public SocialAuthController(
            @Qualifier("kakaoLoginService") SocialLoginService kakaoLoginService,
            @Qualifier("googleLoginService") SocialLoginService googleLoginService,
            @Qualifier("naverLoginService") SocialLoginService naverLoginService,
            TokenProvider tokenProvider,
            UserRepository userRepository) {
        this.kakaoLoginService = kakaoLoginService;
        this.googleLoginService = googleLoginService;
        this.naverLoginService = naverLoginService;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Operation(summary = "카카오 소셜 로그인", description = "카카오 소셜 로그인을 통해 사용자 정보를 받아오고, JWT 토큰을 반환합니다.")
    @PostMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam(name = "code") String code) {
        return processSocialLogin(kakaoLoginService, code);
    }

    @Operation(summary = "구글 소셜 로그인", description = "구글 소셜 로그인을 통해 사용자 정보를 받아오고, JWT 토큰을 반환합니다.")
    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestParam(name = "code") String code) {
        return processSocialLogin(googleLoginService, code);
    }

    @Operation(summary = "네이버 소셜 로그인", description = "네이버 소셜 로그인을 통해 사용자 정보를 받아오고, JWT 토큰을 반환합니다.")
    @PostMapping("/oauth/naver")
    public ResponseEntity<?> naverLogin(@RequestParam(name = "code") String code) {
        return processSocialLogin(naverLoginService, code);
    }

    @Transactional
    private ResponseEntity<?> processSocialLogin(SocialLoginService socialLoginService, String code) {
        try {
            String accessToken = socialLoginService.getAccessToken(code);
            System.out.println("Access Token: " + accessToken);
            User socialUser = socialLoginService.getUserInfo(accessToken);
            System.out.println("Social User Info: " + socialUser);
            User user = userRepository.findBySocialIdAndSocialProvider(socialUser.getSocialId(), socialUser.getSocialProvider())
                    .orElseGet(() -> userRepository.save(socialUser));
            String token = tokenProvider.generateToken(user, Duration.ofHours(2));

            return LoginResponseDto.success(token, user.getRole());
        } catch (Exception e) {
            System.err.println("Error during social login: " + e.getMessage());
            e.printStackTrace();
            return LoginResponseDto.failure();
        }
    }
}
