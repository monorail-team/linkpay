package monorail.linkpay.common;

import monorail.linkpay.MockTestConfiguration;
import monorail.linkpay.linkcard.repository.LinkCardRepository;
import monorail.linkpay.linkedwallet.repository.LinkedMemberRepository;
import monorail.linkpay.linkedwallet.repository.LinkedWalletRepository;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletHistoryRepository;
import monorail.linkpay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MockTestConfiguration.class)
@SpringBootTest
public abstract class IntegrationTest {
    @Autowired
    protected WalletRepository walletRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected WalletHistoryRepository walletHistoryRepository;
    @Autowired
    protected LinkCardRepository linkCardRepository;
    @Autowired
    protected LinkedWalletRepository linkedWalletRepository;
    @Autowired
    protected LinkedMemberRepository linkedMemberRepository;

    protected Member member;

    @Autowired
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        linkCardRepository.deleteAllInBatch();
        linkedMemberRepository.deleteAllInBatch();
        linkedWalletRepository.deleteAllInBatch();
        walletHistoryRepository.deleteAllInBatch();
        walletRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        member = memberRepository.save(createMember());
        walletRepository.save(Wallet.builder()
                .id(idGenerator.generate())
                .member(member)
                .build());
    }

    // todo TestFixtures
    protected Member createMember() {
        return Member.builder()
                .id(idGenerator.generate())
                .email("linkpay@gmail.com")
                .username("link1")
                .build();
    }

}
