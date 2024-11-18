package sg.backend.service.socialLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sg.backend.config.OAuthProperties;
import sg.backend.entity.User;
import sg.backend.entity.Role;

import java.util.Map;


@Service("googleLoginService")
@RequiredArgsConstructor
public class GoogleLoginService implements SocialLoginService{

    private final RestTemplate restTemplate;
    private final OAuthProperties oauthProperties;

    @Override
    public String getAccessToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oauthProperties.getGoogle().getClientId());
        params.add("client_secret", oauthProperties.getGoogle().getClientSecret());
        params.add("redirect_uri", oauthProperties.getGoogle().getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
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
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        String socialId = responseBody.get("sub").toString();

        User socialUser = new User(null, null, null, Role.USER);
        socialUser.setSocialUser(true);
        socialUser.setSocialProvider("Google");
        socialUser.setSocialId(socialId);

        return socialUser;
    }
}
