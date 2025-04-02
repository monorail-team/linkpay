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
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
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
    void 특정_링크지갑을_조회한다() {
        String accessToken = 엑세스_토큰();
        String location = 링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest(
                "링크지갑1", Set.of(1L, 2L, 3L))).header("Location");
        Long linkedWalletId = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

        ExtractableResponse<Response> response = 링크지갑_조회_요청(accessToken, linkedWalletId);
        LinkedWalletResponse linkedWalletResponse = response.as(LinkedWalletResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(linkedWalletResponse).isEqualTo(new LinkedWalletResponse(
                        linkedWalletId.toString(), "링크지갑1", 0L, 4))
        );
    }

    @Test
    void 내가_소유한_링크지갑들을_조회한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑3", Set.of(1L, 2L, 3L)));

        ExtractableResponse<Response> response = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
        LinkedWalletsResponse linkedWalletsResponse = response.as(LinkedWalletsResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(linkedWalletsResponse.linkedWallets()).hasSize(3)
        );
    }

    @Test
    void 내가_참여한_링크지갑들을_조회한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of(1L, 2L, 3L)));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑3", Set.of(1L, 2L, 3L)));

        ExtractableResponse<Response> response = 링크지갑_목록_조회_요청(accessToken, "PARTICIPANT");
        LinkedWalletsResponse linkedWalletsResponse = response.as(LinkedWalletsResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(linkedWalletsResponse.linkedWallets()).isEmpty()
        );
    }

    @Test
    void 링크지갑에_포인트를_충전한다() {
        String accessToken = 엑세스_토큰();
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of(1L, 2L, 3L)));

        String linkedWalletId = 링크지갑_목록_조회_요청(accessToken, "CREATOR").as(LinkedWalletsResponse.class).linkedWallets()
                .getFirst().linkedWalletId();
        ExtractableResponse<Response> response = 링크지갑_충전_요청(accessToken, Long.valueOf(linkedWalletId),
                new WalletPointRequest(10000L));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 링크지갑_충전_요청(final String accessToken, final Long linkedWalletId,
                                                     final WalletPointRequest pointRequest) {
        return sendPatchRequest("/api/linked-wallets/charge/%s".formatted(linkedWalletId), accessToken, pointRequest);
    }

    public static ExtractableResponse<Response> 링크지갑_생성_요청(final String accessToken,
                                                           final LinkedWalletCreateRequest linkedWalletCreateRequest) {
        return sendPostRequest("/api/linked-wallets", accessToken, linkedWalletCreateRequest);
    }

    public static ExtractableResponse<Response> 링크지갑_목록_조회_요청(final String accessToken, final String role) {
        return sendGetRequest("/api/linked-wallets?role=%s".formatted(role), accessToken);
    }

    public static ExtractableResponse<Response> 링크지갑_조회_요청(final String accessToken, Long linkedWalletId) {
        return sendGetRequest("/api/linked-wallets/%s".formatted(linkedWalletId), accessToken);
    }
}
