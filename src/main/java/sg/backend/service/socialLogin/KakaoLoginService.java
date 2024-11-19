package sg.backend.service.socialLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sg.backend.config.OAuthProperties;
import sg.backend.entity.Role;
import sg.backend.entity.User;

import org.springframework.http.HttpHeaders;
import java.util.Map;

@Service("kakaoLoginService")
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService{
    private final RestTemplate restTemplate;
    private final OAuthProperties oauthProperties;

    @Override
    public String getAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oauthProperties.getKakao().getClientId());
        params.add("client_secret", oauthProperties.getKakao().getClientSecret());
        params.add("redirect_uri", oauthProperties.getKakao().getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    @Override
    public User getUserInfo(String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile.get("nickname").toString();
        String socialId = nickname;

        String email = null;
        String phoneNumber = null;
        User socialUser = new User(email, null, phoneNumber, Role.USER);
        socialUser.setSocialUser(true);
        socialUser.setSocialProvider("Kakao");
        socialUser.setSocialId(socialId.toString());

        return socialUser;
    }
}
