package sg.backend.service.socialLogin;

import sg.backend.entity.User;

public interface SocialLoginService {
    String getAccessToken(String code);
    User getUserInfo(String accessToken);
}
