package backend.a105.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
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

        // RestTemplate을 사용하여 POST 요청 보내기
        HttpEntity<String> entity = new HttpEntity<>(null);  // 요청 본문이 없는 경우
        ResponseEntity<KakaoOauthResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                KakaoOauthResponse.class
        );


        return responseEntity;
    }

    public ResponseEntity<KakaoUserResponse> fetchUser(String accessToken) {
        // RestTemplate을 사용하여 GET 요청 보내기
        HttpEntity<String> entity = new HttpEntity<>(null);  // 요청 본문이 없는 경우
        ResponseEntity<KakaoUserResponse> responseEntity = restTemplate.exchange(
                props.userApiUri,
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class,
                "Bearer " + accessToken
        );
        log.info("Fetched user response: {}", responseEntity);
        return responseEntity;
    }
}
