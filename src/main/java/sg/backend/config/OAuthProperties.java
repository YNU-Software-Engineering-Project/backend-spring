package sg.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Data
@Component
@Primary
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
    private Provider kakao;
    private Provider google;
    private Provider naver;

    @Data
    public static class Provider{
        private String clientId;
        private String clientSecret;
        private String redirectUri;
    }
}
