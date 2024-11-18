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

@Service("naverLoginService")
@RequiredArgsConstructor
public class NaverLoginService implements SocialLoginService {
    private final RestTemplate restTemplate;
    private final OAuthProperties oauthProperties;

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", oauthProperties.getNaver().getClientId());
        params.add("client_secret", oauthProperties.getNaver().getClientSecret());
        params.add("redirect_uri", oauthProperties.getNaver().getRedirectUri());
        params.add("code", code);
        params.add("state", "random_string");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://nid.naver.com/oauth2.0/token",
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }

    @Override
    public User getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");

        String name = responseBody.get("name").toString();
        String socialId = name;

        User socialUser = new User(null, null,null, Role.USER);
        socialUser.setSocialUser(true);
        socialUser.setSocialProvider("Naver");
        socialUser.setSocialId(socialId.toString());
        
        return socialUser;
    }
}
