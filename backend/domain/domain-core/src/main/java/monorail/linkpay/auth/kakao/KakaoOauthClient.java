package monorail.linkpay.auth.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauthClient {

    private final RestTemplate restTemplate;
    private final KakaoOauthProps props;

    public ResponseEntity<KakaoOauthResponse> authorize(final String code) {
        String uri = UriComponentsBuilder.fromUriString(props.authorizeApiUri)
                .queryParam("grant_type", props.grantType)
                .queryParam("client_id", props.clientId)
                .queryParam("client_secret", props.clientSecret)
                .queryParam("redirect_uri", props.redirectUrl)
                .queryParam("code", code)
                .toUriString();

        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                KakaoOauthResponse.class
        );
    }

    public ResponseEntity<KakaoUserResponse> fetchUser(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return restTemplate.exchange(
                props.userApiUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoUserResponse.class
        );
    }
}
