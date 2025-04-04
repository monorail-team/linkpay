package monorail.linkpay.acceptance;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.엑세스_토큰;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendDeleteRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendGetRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPatchRequest;
import static monorail.linkpay.acceptance.client.RestAssuredClient.sendPostRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

public class LinkedWalletAcceptanceTest extends AcceptanceTest {

    @Test
    void 링크지갑을_생성한다() {
        String accessToken = 엑세스_토큰();
        ExtractableResponse<Response> response = 링크지갑_생성_요청(accessToken,
                new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 특정_링크지갑을_조회한다() {
        String accessToken = 엑세스_토큰();
        String location = 링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest(
                "링크지갑1", Set.of("1", "2", "3"))).header("Location");
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
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of("1", "2", "3")));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑3", Set.of("1", "2", "3")));

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
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of("1", "2", "3")));
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑3", Set.of("1", "2", "3")));

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
        링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));

        String linkedWalletId = 링크지갑_목록_조회_요청(accessToken, "CREATOR").as(LinkedWalletsResponse.class).linkedWallets()
                .getFirst().linkedWalletId();
        ExtractableResponse<Response> response = 링크지갑_충전_요청(accessToken, Long.valueOf(linkedWalletId),
                new WalletPointRequest(10000L));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @TestFactory
    Stream<DynamicTest> 링크지갑을_생성_후_삭제한다() {
        String accessToken = 엑세스_토큰();
        AtomicReference<LinkedWalletsResponse> linkedWalletsResponse = new AtomicReference<>();
        return Stream.of(
                dynamicTest("링크지갑을 생성한다", () -> {
                    링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑1", Set.of("1", "2", "3")));
                    링크지갑_생성_요청(accessToken, new LinkedWalletCreateRequest("링크지갑2", Set.of("1", "2", "3")));

                    ExtractableResponse<Response> response = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
                    linkedWalletsResponse.set(response.as(LinkedWalletsResponse.class));
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(linkedWalletsResponse.get().linkedWallets()).hasSize(2)
                    );
                }),

                dynamicTest("링크지갑을 삭제한다", () -> {
                    Long linkedWalletId = Long.parseLong(linkedWalletsResponse.get()
                            .linkedWallets()
                            .getFirst().linkedWalletId());
                    ExtractableResponse<Response> response = 링크지갑_삭제_요청(accessToken, linkedWalletId);
                    System.out.println(linkedWalletId + "??");
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                }),

                dynamicTest("링크지갑 조회시 삭제한 링크지갑을 제외한 결과가 나온다", () -> {
                    ExtractableResponse<Response> response = 링크지갑_목록_조회_요청(accessToken, "CREATOR");
                    LinkedWalletsResponse res = response.as(LinkedWalletsResponse.class);
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(res.linkedWallets()).hasSize(1)
                    );
                })
        );
    }

    public static ExtractableResponse<Response> 링크지갑_충전_요청(final String accessToken, final Long linkedWalletId,
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

    private ExtractableResponse<Response> 링크지갑_삭제_요청(final String accessToken, final Long linkedWalletId) {
        return sendDeleteRequest("/api/linked-wallets/" + linkedWalletId, accessToken);
    }
}
