package monorail.linkpay.wallet;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletServiceTest extends IntegrationTest {

    @Autowired
    private WalletService walletService;


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
        walletService.charge(member.getId(), point);

        // then
        WalletResponse response = walletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(50000L);
    }

    @Test
    void 지갑에서_포인트를_차감하고_잔액을_확인한다() {
        // given
        Point point = new Point(50000);
        Point subtractPoint = new Point(30000);
        walletService.charge(member.getId(), point);

        // when
        walletService.deduct(member.getId(), subtractPoint);

        // then
        WalletResponse response = walletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(20000L);
    }

    @Test
    void 잔액보다_많이_차감할경우_예외가_발생한다() {
        // given
        Point point = new Point(50000);
        Point subtractPoint = new Point(50001);
        walletService.charge(member.getId(), point);

        // when
        Throwable exception = assertThrows(LinkPayException.class, () ->
                walletService.deduct(member.getId(), subtractPoint)
        );

        // then
        assertEquals("차감할 금액은 잔액보다 작거나 같은 값이어야 합니다.", exception.getMessage());
    }

    /**
    * @설명
    * 정책 상 한 지갑에 대해 동시 발급 가능한 카드는 100개이므로 여유롭게 동시 1000개 요청까지만 테스트
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
                        walletService.charge(member.getId(), point);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        // then
        WalletResponse response = walletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(point.getAmount() * jobCount);
    }

    /**
     * @설명
     * 정책 상 한 지갑에 대해 동시 발급 가능한 카드는 100개이므로 여유롭게 동시 1000개 요청까지만 테스트
     */
    @Test
    void 지갑에서_여러번_차감한다() throws InterruptedException {
        // given
        Point point = new Point(1);
        walletService.charge(member.getId(), new Point(1001));
        int threadCount = 16;
        int jobCount = 1000;

        // when
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch latch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                executorService.submit(() -> {
                    try {
                        walletService.deduct(member.getId(), point);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        // then
        WalletResponse response = walletService.read(member.getId());
        assertThat(response.amount()).isEqualTo(1L);
    }
}
