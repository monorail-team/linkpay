package monorail.linkpay.controller;

import static monorail.linkpay.controller.ControllerFixture.CHARGE_REQUEST;
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

public class WalletControllerTest extends ControllerTest {

    @Test
    void 지갑을_충전한다() {
        doNothing().when(walletService).charge(anyLong(), any(Point.class));

        docsGiven
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer {access_token}")
                .body(CHARGE_REQUEST)
                .when().patch("/api/wallets/charge")
                .then().log().all()
                .apply(document("wallets/charge"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 지갑_잔액을_조회한다() {
        when(walletService.read(anyLong())).thenReturn(WALLET_RESPONSE);

        docsGiven
                .header("Authorization", "Bearer {access_token}")
                .when().get("/api/wallets")
                .then().log().all()
                .apply(document("wallets/read"))
                .statusCode(HttpStatus.OK.value());
    }
}
