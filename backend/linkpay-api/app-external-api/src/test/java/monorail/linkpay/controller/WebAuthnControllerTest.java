package monorail.linkpay.controller;

import jakarta.servlet.http.HttpSession;
import monorail.linkpay.controller.request.WebAuthnRegisterRequest;
import monorail.linkpay.controller.request.WebAuthnRequest;
import monorail.linkpay.webauthn.dto.WebAuthnChallengeResponse;
import monorail.linkpay.webauthn.dto.WebAuthnResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class WebAuthnControllerTest extends ControllerTest {

    @Test
    void 지문_등록_챌린지를_요청한다() {
        when(webAuthnService.getRegisterChallenge()).thenReturn("{challenge-number}");

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/webauthn/register/challenge")
                .then().log().all()
                .apply(document("webauthn/register-challenge"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 지문을_등록한다() {
        doNothing().when(webAuthnService)
                .registerAuthenticator(anyLong(), anyString(), anyString());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WebAuthnRegisterRequest.builder()
                        .credentialId("credentialId")
                        .attestationObject("attestationObject")
                        .build())
                .when().post("/api/webauthn/register")
                .then().log().all()
                .apply(document("webauthn/register"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 지문_인증_챌린지를_요청한다() {
        when(webAuthnService.getAuthChallenge(anyLong()))
                .thenReturn(WebAuthnChallengeResponse.builder()
                        .challenge("{challenge-number}")
                        .credentialId("{credentialId}")
                .build());

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/webauthn/authenticate/challenge")
                .then().log().all()
                .apply(document("webauthn/authenticate-challenge"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 지문_인증을_수행한다() {
        when(webAuthnService.verifyAuthentication(anyLong(), anyString(), anyString(), anyString(),anyString()))
                .thenReturn(WebAuthnResponse.builder().paymentToken("{paymentToken}").build());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WebAuthnRequest.builder()
                        .credentialId("{credentialId}")
                        .authenticatorData("{authenticatorData}")
                        .clientDataJSON("{clientDataJSON}")
                        .signature("{signature}")
                        .build())
                .when().post("/api/webauthn/authenticate")
                .then().log().all()
                .apply(document("webauthn/authenticate"))
                .statusCode(HttpStatus.OK.value());
    }
}
