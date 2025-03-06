package backend.a105.auth;

import backend.a105.layer.SupportLayer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@SupportLayer
@RequiredArgsConstructor
public class KakaoLoginProcessor {
    private final KakaoOauthClint kakaoOauthClint;

    public Email process(String code) {
        ResponseEntity<String> response = kakaoOauthClint.authenticate(code);
        if(response.getStatusCode().is4xxClientError()){
            // thrdow
        }

        return Email.of("email");
    }
}
