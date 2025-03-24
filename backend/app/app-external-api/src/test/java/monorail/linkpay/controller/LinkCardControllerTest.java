package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.LINK_CARD_CREATE_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

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
}
