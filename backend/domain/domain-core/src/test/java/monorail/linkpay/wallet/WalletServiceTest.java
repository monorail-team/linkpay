package monorail.linkpay.wallet;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import monorail.linkpay.wallet.service.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

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