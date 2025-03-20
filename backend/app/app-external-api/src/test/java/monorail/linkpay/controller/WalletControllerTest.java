package monorail.linkpay.controller;

import monorail.linkpay.controller.request.ChargeRequest;
import monorail.linkpay.wallet.service.WalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class WalletControllerTest extends ControllerTest {

    @Test
    void 지갑을_충전한다() {
        doNothing().when(walletService).charge(anyLong(), anyLong());

        docsGiven
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer {access_token}")
            .body(new ChargeRequest(50000))
            .when().patch("/api/wallets/charge")
            .then().log().all()
            .apply(document("wallets/charge"))
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 지갑_잔액을_조회한다() {
        when(walletService.read(anyLong())).thenReturn(new WalletResponse(50000));

        docsGiven
            .header("Authorization", "Bearer {access_token}")
            .when().get("/api/wallets")
            .then().log().all()
            .apply(document("wallets/read"))
            .statusCode(HttpStatus.OK.value());
    }
}
