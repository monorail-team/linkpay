package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINK_CARD_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.CardType;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LinkCardControllerTest extends ControllerTest {

    @Test
    void 링크카드를_생성한다() {
        doNothing().when(linkCardService).create(anyLong(), any(CreateLinkCardServiceRequest.class));

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
    void 보유한_링크카드를_조회한다() {
        List<LinkCardResponse> linkCardResponse = new ArrayList<>();
        linkCardResponse.add(new LinkCardResponse(1L, 500000L,
                CardType.OWNED.name(),
                CardColor.getRandomColor().getHexCode(),
                "test card",
                LocalDate.now().plusMonths(1),
                0L));
        LinkCardsResponse response = new LinkCardsResponse(linkCardResponse, false);

        when(linkCardService.read(anyLong(), nullable(Long.class), eq(10))).thenReturn(response);

        docsGiven.header("Authorization", "Bearer {access_token}")
                .when().get("/api/cards")
                .then().log().all()
                .apply(document("cards/read"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 보유한_링크카드_중_등록안된_링크카드를_조회한다() {
        List<LinkCardResponse> linkCardResponse = new ArrayList<>();
        linkCardResponse.add(new LinkCardResponse(1L, 500000L,
                CardType.OWNED.name(),
                CardColor.getRandomColor().getHexCode(),
                "test card",
                LocalDate.now().plusMonths(1),
                0L));
        LinkCardsResponse response = new LinkCardsResponse(linkCardResponse, false);

        when(linkCardService.readUnregister(anyLong(), nullable(Long.class), eq(10))).thenReturn(response);

        docsGiven.header("Authorization", "Bearer {access_token}")
                .when().get("/api/cards/unregister")
                .then().log().all()
                .apply(document("cards/read/unregister"))
                .statusCode(HttpStatus.OK.value());
    }
}
