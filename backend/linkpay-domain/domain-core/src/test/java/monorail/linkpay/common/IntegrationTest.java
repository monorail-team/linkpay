package monorail.linkpay.common;

import monorail.linkpay.DatabaseCleaner;
import monorail.linkpay.MockTestConfiguration;
import monorail.linkpay.history.repository.WalletHistoryRepository;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.payment.repository.PaymentRepository;
import monorail.linkpay.store.domain.Store;
import monorail.linkpay.store.repository.StoreRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;
import monorail.linkpay.wallet.repository.MyWalletRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MockTestConfiguration.class)
@SpringBootTest
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
    private IdGenerator idGenerator;

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

    protected Store createStore() {
        return Store.builder()
                .id(1L)
                .name("store1")
                .build();
    }
}
