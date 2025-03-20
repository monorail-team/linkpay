package monorail.linkpay.acceptance;

import monorail.linkpay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static monorail.linkpay.acceptance.AuthAcceptanceTest.*;

public class WalletAcceptanceTest extends AcceptanceTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void 지갑_충전_요청() {
        String accessToken = 엑세스_토큰();
        Re
    }

}
