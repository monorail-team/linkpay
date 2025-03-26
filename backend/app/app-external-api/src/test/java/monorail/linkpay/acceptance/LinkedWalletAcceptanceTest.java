package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPatchRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Set;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.linkedwallet.dto.LinkedWalletResponse;
import monorail.linkpay.linkedwallet.dto.LinkedWalletsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class LinkedWalletAcceptanceTest extends AcceptanceTest {

    @Test
    void 링크지갑을_생성한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 링크지갑_생성_요청(accessToken,
                new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 나의_링크지갑들을_조회한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑3", Set.of(1L, 2L, 3L)));

        ExtractableResponse<Response> response = 링크지갑_목록_조회_요청(accessToken);
        LinkedWalletsResponse linkedWalletsResponse = response.as(LinkedWalletsResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(linkedWalletsResponse.linkedWallets()).hasSize(3)
        );
    }

    @Test
    void 링크지갑에_포인트를_충전한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));

        Long linkedWalletId = 링크지갑_목록_조회_요청(accessToken).as(LinkedWalletsResponse.class).linkedWallets()
                .getFirst().linkedWalletId();

        ExtractableResponse<Response> response = 링크지갑_충전_요청(accessToken, linkedWalletId,
                new WalletPointRequest(10000L));
        LinkedWalletResponse linkedWalletResponse = 링크지갑_목록_조회_요청(accessToken).as(LinkedWalletsResponse.class)
                .linkedWallets().getFirst();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(linkedWalletResponse.linkedWalletId()).isEqualTo(linkedWalletId),
                () -> assertThat(linkedWalletResponse.amount()).isEqualTo(10000L)
        );
    }

    @Test
    void 링크지갑에서_포인트를_차감한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));

        Long linkedWalletId = 링크지갑_목록_조회_요청(accessToken).as(LinkedWalletsResponse.class).linkedWallets()
                .getFirst().linkedWalletId();

        링크지갑_충전_요청(accessToken, linkedWalletId, new WalletPointRequest(50000L));

        ExtractableResponse<Response> response = 링크지갑_차감_요청(accessToken, linkedWalletId,
                new WalletPointRequest(20000L));
        LinkedWalletResponse linkedWalletResponse = 링크지갑_목록_조회_요청(accessToken).as(LinkedWalletsResponse.class)
                .linkedWallets().getFirst();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(linkedWalletResponse.linkedWalletId()).isEqualTo(linkedWalletId),
                () -> assertThat(linkedWalletResponse.amount()).isEqualTo(30000L)
        );
    }

    private ExtractableResponse<Response> 링크지갑_차감_요청(final String accessToken, final Long linkedWalletId,
                                                     final WalletPointRequest pointRequest) {
        return sendPatchRequest("/api/linked-wallets/deduct/%s".formatted(linkedWalletId), accessToken, pointRequest);
    }

    private ExtractableResponse<Response> 링크지갑_충전_요청(final String accessToken, final Long linkedWalletId,
                                                     final WalletPointRequest pointRequest) {
        return sendPatchRequest("/api/linked-wallets/charge/%s".formatted(linkedWalletId), accessToken, pointRequest);
    }

    private ExtractableResponse<Response> 링크지갑_생성_요청(final String accessToken,
                                                     final LinkedWalletCreateRequest linkedWalletCreateRequest) {
        return sendPostRequest("/api/linked-wallets", accessToken, linkedWalletCreateRequest);
    }

    private ExtractableResponse<Response> 링크지갑_목록_조회_요청(final String accessToken) {
        return sendGetRequest("/api/linked-wallets", accessToken);
    }
}
