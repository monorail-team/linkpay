package monorail.linkpay.common;

import monorail.linkpay.DatabaseCleaner;
import monorail.linkpay.MockTestConfiguration;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.fcm.repository.FcmTokenRepository;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import monorail.linkpay.linkcard.domain.CardColor;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.repository.StoreRepository;
import monorail.linkpay.store.repository.StoreSignerRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static monorail.linkpay.linkcard.domain.CardState.UNREGISTERED;
import static monorail.linkpay.linkcard.domain.CardType.SHARED;

@Import(MockTestConfiguration.class)
@SpringBootTest(
        properties = "banking.account.uri=http://localhost:8080/api/bank-account"
)
public abstract class IntegrationTest {

    @Autowired
    protected DatabaseCleaner databaseCleaner;
    @Autowired
    protected MyWalletRepository myWalletRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected WalletHistoryRepository walletHistoryRepository;
    @Autowired
    protected LinkCardRepository linkCardRepository;
    @Autowired
    protected LinkedWalletRepository linkedWalletRepository;
    @Autowired
    protected WalletRepository walletRepository;
    @Autowired
    protected LinkedMemberRepository linkedMemberRepository;
    @Autowired
    protected PaymentRepository paymentRepository;
    @Autowired
    protected StoreRepository storeRepository;
    @Autowired
    protected StoreSignerRepository storeSignerRepository;
    @Autowired
    protected FcmTokenRepository fcmTokenRepository;
    @Autowired
    protected IdGenerator idGenerator;

    protected Member member;

    @BeforeEach
    void setUp() {
        databaseCleaner.truncateAllTables();
        member = memberRepository.save(
                createMember("linkpay@gmail.com", "linkpay"));
        myWalletRepository.save(MyWallet.builder()
                .id(idGenerator.generate())
                .member(member)
                .build());
        storeRepository.save(createStore());
    }

    protected Member createMember(final String email, final String username) {
        return Member.builder()
                .id(idGenerator.generate())
                .email(email)
                .username(username)
                .build();
    }

    protected LinkedWallet createLinkedWallet(final String name) {
        return LinkedWallet.builder()
                .id(idGenerator.generate())
                .name(name)
                .build();
    }

    protected Store createStore() {
        return Store.builder()
                .id(1L)
                .name("store1")
                .build();
    }

    protected LinkedMember createLinkedMember(final Role role,
                                              final Member member,
                                              final LinkedWallet linkedWallet) {
        return LinkedMember.builder()
                .id(idGenerator.generate())
                .role(role)
                .member(member)
                .linkedWallet(linkedWallet)
                .build();
    }

    protected LinkCard createLinkWalletCard(final LinkedWallet linkedWallet,
                                            final Member member) {
        return LinkCard.builder()
                .id(idGenerator.generate())
                .cardColor(CardColor.getRandomColor())
                .cardName("cardName")
                .cardType(SHARED)
                .wallet(linkedWallet)
                .limitPrice(new Point(50000000))
                .member(member)
                .expiredAt(LocalDate.now().plusDays(1).atStartOfDay())
                .usedPoint(new Point(0))
                .state(UNREGISTERED)
                .build();
    }
}
