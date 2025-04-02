package monorail.linkpay.wallet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.service.MyWalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MyWalletServiceTest extends IntegrationTest {

    @Autowired
    private MyWalletService myWalletService;

//    @Test
//    void 지갑을_생성한다() {
//        // when
//        Long walletId = walletService.create(member);
//
//        // then
//        Wallet result = walletRepository.findById(walletId).orElseThrow();
//        assertThat(result).isNotNull();
//    }

    @Test
    void 지갑을_충전하고_잔액을_확인한다() {
        // given
        Point point = new Point(50000);

        // when
        myWalletService.charge(member.getId(), point);

        // then
        WalletResponse response = myWalletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(50000L);
    }

    /**
     * @설명 정책 상 한 지갑에 대해 동시 발급 가능한 카드는 100개이므로 여유롭게 동시 1000개 요청까지만 테스트
     */
    @Test
    void 지갑을_여러번_충전하고_잔액을_확인한다() throws InterruptedException {
        // given
        Point point = new Point(1);
        int threadCount = 16;
        int jobCount = 100;

        // when
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch latch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                executorService.submit(() -> {
                    try {
                        myWalletService.charge(member.getId(), point);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        // then
        WalletResponse response = myWalletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(point.getAmount() * jobCount);
    }
}
