package monorail.linkpay.history;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import monorail.linkpay.common.IntegrationTest;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.history.service.WalletHistoryService;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.service.LinkedWalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WalletHistoryServiceTest extends IntegrationTest {

    @Autowired
    private WalletHistoryService walletHistoryService;
    @Autowired
    private LinkedWalletService linkedWalletService;

    @Test
    void 내_지갑의_특정_히스토리를_조회한다() {
        // given
        MyWallet wallet = myWalletRepository.findByMemberId(member.getId()).orElseThrow();

        WalletHistory history = createWalletHistory(wallet, new Point(50), new Point(50), DEPOSIT,
                member);
        walletHistoryRepository.save(history);

        // when
        WalletHistoryResponse response = walletHistoryService.readWalletHistory(history.getId(), member.getId());

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void 지갑히스토리_조회시_내_지갑이_아닌경우_오류가_발생한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        memberRepository.save(member1);
        myWalletRepository.save(MyWallet.builder()
                .id(idGenerator.generate())
                .member(member1)
                .build());

        MyWallet othersWallet = myWalletRepository.findByMemberId(member1.getId()).orElseThrow();
        WalletHistory history = createWalletHistory(othersWallet, new Point(50), new Point(50), DEPOSIT,
                member);
        walletHistoryRepository.save(history);

        // when // then
        assertThatThrownBy(() -> walletHistoryService.readWalletHistory(history.getId(), member.getId())).isInstanceOf(
                LinkPayException.class).hasMessage("소유한 지갑이 아닙니다.");

    }

    @Test
    void 내가_속한_링크지갑의_특정_히스토리를_조회한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        String walletName = "링크지갑";

        memberRepository.saveAll(List.of(member1));
        Long linkedWalletId = linkedWalletService.createLinkedWallet(member.getId(), "name", Set.of(member1.getId()));
        LinkedWallet linkedWallet = linkedWalletRepository.findById(linkedWalletId).orElseThrow();
//        linkedMemberRepository.saveAll(List.of(
//                createLinkedMember(CREATOR, member, linkedWallet),
//                createLinkedMember(PARTICIPANT, member1, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(linkedWallet, member),
                        createLinkWalletCard(linkedWallet, member1)));
        WalletHistory history = createWalletHistory(linkedWallet, new Point(50), new Point(50), DEPOSIT,
                member);
        walletHistoryRepository.save(history);

        // when
        WalletHistoryResponse response = walletHistoryService.readWalletHistory(history.getId(), member1.getId());

        // then
        assertThat(response).isNotNull();
    }

    @Test
    void 내가_속한_링크지갑이_아닌_지갑의_히스토리_조회시_오류가_발생한다() {
        // given
        Member member1 = createMember("test@email.com", "u1");
        Member member2 = createMember("test2@email.com", "u2");

        memberRepository.saveAll(List.of(member1, member2));
        String walletName = "링크지갑";
        linkedWalletRepository.save(LinkedWallet.builder().id(1L).name(walletName).build());
        LinkedWallet linkedWallet = linkedWalletRepository.findById(1L).orElseThrow();
        linkedMemberRepository.saveAll(List.of(
                createLinkedMember(CREATOR, member, linkedWallet),
                createLinkedMember(PARTICIPANT, member1, linkedWallet)));
        linkCardRepository.saveAll(
                List.of(createLinkWalletCard(linkedWallet, member),
                        createLinkWalletCard(linkedWallet, member1)));
        WalletHistory history = createWalletHistory(linkedWallet, new Point(50), new Point(50), DEPOSIT,
                member);
        walletHistoryRepository.save(history);

        // when // then
        assertThatThrownBy(() -> walletHistoryService.readWalletHistory(history.getId(), member2.getId())).isInstanceOf(
                LinkPayException.class).hasMessage("참여중인 링크지갑이 아닙니다.");
    }

    private WalletHistory createWalletHistory(final Wallet wallet,
                                              final Point amount,
                                              final Point remain,
                                              final TransactionType type,
                                              final Member member) {
        return WalletHistory.builder()
                .id(idGenerator.generate())
                .wallet(wallet)
                .amount(amount)
                .remaining(remain)
                .transactionType(type)
                .member(member)
                .build();
    }
}
