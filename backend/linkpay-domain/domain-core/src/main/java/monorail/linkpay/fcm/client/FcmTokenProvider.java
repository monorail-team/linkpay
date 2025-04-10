package monorail.linkpay.fcm.client;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.exception.ExceptionCode;
import monorail.linkpay.exception.LinkPayException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmTokenProvider {

    private final FcmProps props;
    private AccessToken cachedToken;

    public String getAccessToken() {
        if (cachedToken == null || cachedToken.getExpirationTime().before(new Date())) {
            try {
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(new ByteArrayInputStream(
                                props.serviceAccountKey().value().getBytes(StandardCharsets.UTF_8)))
                        .createScoped("https://www.googleapis.com/auth/firebase.messaging");

                credentials.refreshIfExpired();
                cachedToken = credentials.getAccessToken();
            } catch (IOException e) {
                LinkPayException linkPayException = new LinkPayException(ExceptionCode.SERVER_ERROR, "FCM 인증 토큰 발급 실패");
                linkPayException.initCause(e);
                throw linkPayException;
            }

        }
        return cachedToken.getTokenValue();
    }
}
