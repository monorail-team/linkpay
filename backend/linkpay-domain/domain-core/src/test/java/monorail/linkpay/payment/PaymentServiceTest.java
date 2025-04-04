package monorail.linkpay.payment;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.linkcard.service.request.LinkCardCreateServiceRequest;
import monorail.linkpay.payment.service.PaymentService;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.domain.StoreSigner;
import monorail.linkpay.store.service.StoreService;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.service.MyWalletService;
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
    @Autowired
    private StoreService storeService;

    @Test
    void 내지갑_링크카드로_결제한다() {
        // given
        myWalletService.charge(member.getId(), new Point(50000));

        LinkCardCreateServiceRequest linkCardCreateServiceRequest = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), linkCardCreateServiceRequest);
        List<LinkCard> linkCards = linkCardRepository.findByMemberId(member.getId());
        LinkCard linkCard = linkCards.getFirst();
        Long storeId = storeService.create("newStore");
        Store store = storeRepository.findById(storeId).orElseThrow();
        StoreSigner storeSigner = storeSignerRepository.findByStoreId(storeId).orElseThrow();
        var txInfo = PaymentFixture.txInfo(store, storeSigner, new Point(30000));
        var payInfo = PaymentFixture.payInfo(member, linkCard);
        // TODO: 테스트 깨지는 거 수정
        // when
        paymentService.createPayment(txInfo, payInfo);

        // then
        WalletResponse walletResponse = myWalletService.read(member.getId());
        assertThat(walletResponse.amount()).isEqualTo(20000L);
    }

    @Test
    void 내지갑_링크카드로_여러번_결제한다() throws InterruptedException {
        // given
        Point chargePoint = new Point(1000);
        myWalletService.charge(member.getId(), chargePoint);

        LinkCardCreateServiceRequest linkCardCreateServiceRequest = createCard(LocalDate.now().plusDays(1));
        linkCardService.create(member.getId(), linkCardCreateServiceRequest);
        List<LinkCard> linkCards = linkCardRepository.findByMemberId(member.getId());
        LinkCard linkCard = linkCards.getFirst();

        Long storeId = storeService.create("newStore");
        Store store = storeRepository.findById(storeId).orElseThrow();
        StoreSigner storeSigner = storeSignerRepository.findByStoreId(storeId).orElseThrow();


        // when
        int threadCount = 16;
        int jobCount = 100;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch countDownLatch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                executorService.submit(() -> {
                    try {
                        var txInfo = PaymentFixture.txInfo(store, storeSigner, new Point(1));
                        var payInfo = PaymentFixture.payInfo(member, linkCard);
                        paymentService.createPayment(txInfo, payInfo);
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
                .isEqualTo(900L);
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
        List<LinkCard> linkCards = linkCardRepository.findByMemberId(member.getId());

        Long storeId = storeService.create("newStore");
        Store store = storeRepository.findById(storeId).orElseThrow();
        StoreSigner storeSigner = storeSignerRepository.findByStoreId(storeId).orElseThrow();

        // when
        int threadCount = 16;
        int jobCount = 100;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch countDownLatch = new CountDownLatch(jobCount);
            for (int i = 0; i < jobCount; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        LinkCard linkCard = linkCards.get(index % linkCardAmount);
                        var txInfo = PaymentFixture.txInfo(store, storeSigner, new Point(1));
                        var payInfo = PaymentFixture.payInfo(member, linkCard);
                        paymentService.createPayment(txInfo, payInfo);
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