package monorail.linkpay.payment;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.payment.service.PaymentService;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.service.MyWalletService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest extends IntegrationTest {

    @Autowired
    private MyWalletService myWalletService;
    @Autowired
    private LinkCardService linkCardService;
    @Autowired
    private PaymentService paymentService;

    @Test
    void 내지갑_링크카드로_결제한다() {
        // given
        Point point = new Point(50000);
        Point subtractPoint = new Point(30000);
        myWalletService.charge(member.getId(), point);
        LinkCardCreateServiceRequest linkCardCreateServiceRequest = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), linkCardCreateServiceRequest);
        List<LinkCard> linkCards = linkCardRepository.findLinkCardsByMember(member);
        LinkCard linkCard = linkCards.getFirst();

        // when
        paymentService.createPayment(member.getId(), subtractPoint, linkCard.getId(), 1L);

        // then
        WalletResponse walletResponse = myWalletService.read(member.getId());
        assertThat(walletResponse.amount()).isEqualTo(20000L);
    }

    @Test
    void 내지갑_링크카드로_여러번_결제한다() throws InterruptedException {
        // given
        Point chargePoint = new Point(1000);
        Point subtractPoint = new Point(1);
        myWalletService.charge(member.getId(), chargePoint);

        LinkCardCreateServiceRequest linkCardCreateServiceRequest = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), linkCardCreateServiceRequest);
        List<LinkCard> linkCards = linkCardRepository.findLinkCardsByMember(member);
        LinkCard linkCard = linkCards.getFirst();

        // when
        int threadCount = 16;
        int jobCount = 100;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch countDownLatch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                executorService.submit(() -> {
                    try {
                        paymentService.createPayment(member.getId(), subtractPoint, linkCard.getId(), 1L);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
        }

        // then
        WalletResponse walletResponse = myWalletService.read(member.getId());
        assertThat(walletResponse.amount())
                .isEqualTo(chargePoint.getAmount() - subtractPoint.getAmount() * jobCount);
    }

    @Test
    void 내지갑_링크카드_여러개로_여러번_결제한다() throws InterruptedException {
        // given
        Point chargePoint = new Point(1000);
        Point subtractPoint = new Point(1);
        myWalletService.charge(member.getId(), chargePoint);

        LinkCardCreateServiceRequest linkCardCreateServiceRequest = createCard(LocalDate.now().plusDays(1));
        int linkCardAmount = 10;
        for (int i = 0; i < linkCardAmount; i++) {
            linkCardService.create(member.getId(), linkCardCreateServiceRequest);
        }
        List<LinkCard> linkCards = linkCardRepository.findLinkCardsByMember(member);

        // when
        int threadCount = 16;
        int jobCount = 100;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch countDownLatch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        paymentService.createPayment(member.getId(), subtractPoint,
                                linkCards.get(index % linkCardAmount).getId(), 1L);
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
        }

        // then
        WalletResponse walletResponse = myWalletService.read(member.getId());
        assertThat(walletResponse.amount())
                .isEqualTo(chargePoint.getAmount() - subtractPoint.getAmount() * jobCount);
    }

    private static LinkCardCreateServiceRequest createCard(LocalDate date) {
        return LinkCardCreateServiceRequest.builder()
                .cardName("test card")
                .expiredAt(date)
                .limitPrice(new Point(500000))
                .build();
    }
}