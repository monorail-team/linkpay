package backend.a105.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
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

    public ResponseEntity<KakaoOauthResponse> authorize(String code) {
        String uri = UriComponentsBuilder.fromUriString(props.authorizeApiUri)
                .queryParam("grant_type", props.grantType)
                .queryParam("client_id", props.clientId)
                .queryParam("client_secret", props.clientSecret)
                .queryParam("redirect_uri", props.redirectUrl)
                .queryParam("code", code)
                .toUriString();


        ResponseEntity<KakaoOauthResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                KakaoOauthResponse.class
        );

        return responseEntity;
    }

    public ResponseEntity<KakaoUserResponse> fetchUser(String accessToken) {
        ResponseEntity<KakaoUserResponse> responseEntity = restTemplate.exchange(
                props.userApiUri,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                KakaoUserResponse.class,
                "Bearer " + accessToken
        );
        log.info("Fetched user response: {}", responseEntity);
        return responseEntity;
    }
}
