package monorail.linkpay.auth.kakao;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOauthProps {

    String userApiUri;
    String authorizeApiUri;
    String clientId;
    String clientSecret;
    String redirectUrl;
    String grantType;

    @Builder
    protected KakaoOauthProps(
            final String userApiUri,
            final String authorizeApiUri,
            final String clientId,
            final String clientSecret,
            final String redirectUrl,
            final String grantType
    ) {
        this.userApiUri = userApiUri;
        this.authorizeApiUri = authorizeApiUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
        this.grantType = grantType;
    }
}
