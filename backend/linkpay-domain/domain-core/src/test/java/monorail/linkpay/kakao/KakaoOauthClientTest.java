package monorail.linkpay.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import monorail.linkpay.auth.dto.KakaoUserResponse;
import monorail.linkpay.auth.kakao.KakaoOauthClient;
import monorail.linkpay.auth.kakao.KakaoOauthProps;
import monorail.linkpay.auth.kakao.dto.KakaoOauthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

class KakaoOauthClientTest {

    private KakaoOauthProps props = KakaoOauthProps.builder()
            .authorizeApiUri("https://kakao.auth.kakao.com/oauth/authorize")
            .userApiUri("https://kakao.auth.kakao.com/oauth/user")
            .clientId("kakao")
            .clientSecret("kakao")
            .grantType("authorization_code")
            .build();
    private RestTemplate restTemplate = new RestTemplate();
    private MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();

    @Test
    void 카카오_OAuth_인증_요청이_성공한다() {
        // Given
        String code = "test_auth_code";
        String expectedResponse = """
                    {"access_token": "test_access_token"}
                """;

        String expectedUri = UriComponentsBuilder.fromUriString(props.getAuthorizeApiUri())
                .queryParam("grant_type", props.getGrantType())
                .queryParam("client_id", props.getClientId())
                .queryParam("client_secret", props.getClientSecret())
                .queryParam("redirect_uri", props.getRedirectUrl())
                .queryParam("code", code)
                .toUriString();

        mockServer.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));
        KakaoOauthClient sut = new KakaoOauthClient(restTemplate, props);

        // When
        var response = sut.authorize(code);

        // Then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> mockServer.verify()
        );
        KakaoOauthResponse body = response.getBody();
        assertAll(
                () -> assertThat(body).isNotNull(),
                () -> assertThat("test_access_token").isEqualTo(body.accessToken())
        );
    }

    @Test
    void 카카오_OAuth_유저_정보_요청이_성공한다() {
        // Given
        String accessToken = "test_access_token";
        String expectedResponse = """
                    {
                        "kakao_account": {
                            "email": "test@kakao.com",
                            "nickname": "test"
                        }
                    }
                """;

        mockServer.expect(requestTo(props.getUserApiUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));
        KakaoOauthClient sut = new KakaoOauthClient(restTemplate, props);

        // When

        var response = sut.fetchUser(accessToken);

        // Then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> mockServer.verify()
        );
        KakaoUserResponse body = response.getBody();
        assertAll(
                () -> assertThat(body).isNotNull(),
                () -> assertThat(body.kakaoAccount().email()).isEqualTo("test@kakao.com")
        );
    }
}