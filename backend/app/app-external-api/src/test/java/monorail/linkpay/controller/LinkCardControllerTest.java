package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINK_CARDS_RESPONSE_2;
import static monorail.linkpay.controller.ControllerFixture.LINK_CARD_CREATE_REQUEST;
import static monorail.linkpay.controller.ControllerFixture.LINK_CARD_REGISTRATION_REQUEST;
import static monorail.linkpay.controller.ControllerFixture.SHARED_LINK_CARD_CREATE_REQUEST;
import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.time.LocalDateTime;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.linkcard.service.request.SharedLinkCardCreateServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LinkCardControllerTest extends ControllerTest {

    @Test
    void 링크카드를_생성한다() {
        doNothing().when(linkCardService).create(anyLong(), any(LinkCardCreateServiceRequest.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(LINK_CARD_CREATE_REQUEST)
                .when().post("/api/cards")
                .then().log().all()
                .apply(document("cards/create"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 링크지갑에서_링크카드를_생성한다() {
        doNothing().when(linkCardService).createShared(any(SharedLinkCardCreateServiceRequest.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(SHARED_LINK_CARD_CREATE_REQUEST)
                .when().post("/api/cards/shared")
                .then().log().all()
                .apply(document("cards/create/shared"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 보유한_링크카드를_조회한다() {
        when(linkCardService.read(anyLong(), nullable(Long.class), eq(10))).thenReturn(LINK_CARDS_RESPONSE_2);

        docsGiven.header("Authorization", "Bearer {access_token}")
                .when().get("/api/cards")
                .then().log().all()
                .apply(document("cards/read"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 보유한_링크카드_중_등록안된_링크카드를_조회한다() {
        when(linkCardService.readByState(anyLong(), nullable(Long.class), eq(10), eq(UNREGISTERED),
                any(LocalDateTime.class))).thenReturn(
                LINK_CARDS_RESPONSE_2);

        docsGiven.header("Authorization", "Bearer {access_token}")
                .when().get("/api/cards/unregistered")
                .then().log().all()
                .apply(document("cards/read/deactivate"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 링크카드를_결제카드로_등록한다() {
        doNothing().when(linkCardService).activateLinkCard(anyList());

        docsGiven.contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(LINK_CARD_REGISTRATION_REQUEST)
                .when().patch("/api/cards/activate")
                .then().log().all()
                .apply(document("cards/activate"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 결제카드로_등록된_링크카드를_조회한다() {
        when(linkCardService.readByState(anyLong(), nullable(Long.class), eq(10), eq(UNREGISTERED),
                any(LocalDateTime.class))).thenReturn(
                LINK_CARDS_RESPONSE_2);

        docsGiven.header("Authorization", "Bearer {access_token}")
                .when().get("/api/cards/registered")
                .then().log().all()
                .apply(document("cards/read/activate"))
                .statusCode(HttpStatus.OK.value());
    }
}
