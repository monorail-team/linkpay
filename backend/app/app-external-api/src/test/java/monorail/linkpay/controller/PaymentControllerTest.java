package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.PAYMENT_REQUEST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import monorail.linkpay.common.domain.Point;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PaymentControllerTest extends ControllerTest {

    @Test
    void 링크카드로_결제한다() {
        doNothing().when(paymentService).createPayment(anyLong(), any(Point.class), anyLong(), anyLong());

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(PAYMENT_REQUEST)
                .when().post("/api/payments")
                .then().log().all()
                .apply(document("payments/create"))
                .statusCode(HttpStatus.CREATED.value());
    }
}
