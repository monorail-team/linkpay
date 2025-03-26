package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.WALLET_POINT_REQUEST;
import static monorail.linkpay.controller.ControllerFixture.WALLET_RESPONSE;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import monorail.linkpay.common.domain.Point;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MyWalletControllerTest extends ControllerTest {

    @Test
    void 지갑을_충전한다() {
        doNothing().when(myWalletService).charge(anyLong(), any(Point.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WALLET_POINT_REQUEST)
                .when().patch("/api/wallets/charge")
                .then().log().all()
                .apply(document("wallets/charge"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 지갑잔액을_차감한다() {
        doNothing().when(myWalletService).deduct(anyLong(), any(Point.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(WALLET_POINT_REQUEST)
                .when().patch("/api/wallets/deduct")
                .then().log().all()
                .apply(document("wallets/deduct"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 지갑_잔액을_조회한다() {
        when(myWalletService.read(anyLong())).thenReturn(WALLET_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallets")
                .then().log().all()
                .apply(document("wallets/read"))
                .statusCode(HttpStatus.OK.value());
    }
}
