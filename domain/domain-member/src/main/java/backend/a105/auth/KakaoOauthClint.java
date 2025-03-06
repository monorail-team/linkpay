package backend.a105.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class KakaoOauthClint {
    public ResponseEntity<String> authenticate(String code) {
        return null;
    }
}
