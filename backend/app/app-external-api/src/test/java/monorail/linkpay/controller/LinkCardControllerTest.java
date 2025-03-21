package monorail.linkpay.controller;

import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class LinkCardControllerTest extends ControllerTest {

    @Test
    void 링크카드를_생성한다() {
        doNothing().when(linkCardService).create(anyLong(), any(CreateLinkCardServiceRequest.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(new LinkCardCreateRequest(
                        "테스트카드", 500000, LocalDate.of(2025, 5, 25)))
                .when().post("/api/cards")
                .then().log().all()
                .apply(document("cards/create"))
                .statusCode(HttpStatus.CREATED.value());
    }
}
