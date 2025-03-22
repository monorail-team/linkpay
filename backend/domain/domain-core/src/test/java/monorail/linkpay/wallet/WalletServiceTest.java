package monorail.linkpay.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import monorail.linkpay.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WalletServiceTest extends IntegrationTest {

    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        walletHistoryRepository.deleteAllInBatch();
        walletRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        member = memberRepository.save(createMember());
    }

    @Test
    void 지갑을_생성한다() {
        // when
        Long walletId = walletService.create(member);

        // then
        Wallet result = walletRepository.findById(walletId).orElseThrow();
        assertThat(result).isNotNull();
    }

    @Test
    void 지갑을_충전하고_잔액을_확인한다() {
        // given
        Point point = new Point(50000);
        walletRepository.save(createWallet(member));

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
        walletRepository.save(createWallet(member));
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
        walletRepository.save(createWallet(member));
        walletService.charge(member.getId(), point);

        // when
        Throwable exception = assertThrows(LinkPayException.class, () ->
                walletService.deduct(member.getId(), subtractPoint)
        );

        // then
        assertEquals("차감할 금액은 잔액보다 작거나 같은 값이어야 합니다.", exception.getMessage());
    }

    @Test
    void 지갑을_여러번_충전하고_잔액을_확인한다() throws InterruptedException {
        // given
        Point point = new Point(1000);
        walletRepository.save(createWallet(member));
        int threadCount = 16;
        int jobCount = 10000;

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

    @Test
    void 지갑에서_여러번_차감한다() throws InterruptedException {
        // given
        Point point = new Point(1000);
        walletRepository.save(createWallet(member));
        walletService.charge(member.getId(), new Point(10000));
        int threadCount = 16;
        int jobCount = 10000;

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
        assertThat(response.amount()).isEqualTo(0L);
    }

    private Wallet createWallet(Member member) {
        return Wallet.builder()
                .id(1L)
                .point(new Point(0))
                .member(member)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(1L)
                .email("linkpay@gmail.com")
                .username("link1")
                .build();
    }
}
