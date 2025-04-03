package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import monorail.linkpay.wallet.repository.WalletRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static monorail.linkpay.common.domain.TransactionType.DEPOSIT;
import static monorail.linkpay.common.domain.TransactionType.WITHDRAWAL;
import static monorail.linkpay.exception.ExceptionCode.INVALID_REQUEST;
import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

@SupportLayer
@RequiredArgsConstructor
public class WalletUpdater {

    private final WalletHistoryRecorder walletHistoryRecorder;
    private final WalletRepository walletRepository;
    private final IdGenerator idGenerator;
    private final LinkedMemberRepository linkedMemberRepository;
    private final MemberFetcher memberFetcher;

    public WalletHistory chargePoint(final Wallet wallet, final Point amount, final Member member) {
        wallet.chargePoint(amount);
        return walletHistoryRecorder.recordHistory(DEPOSIT, wallet, amount, member);
    }

    public WalletHistory deductPoint(final Wallet wallet, final Point amount, final Member member) {
        wallet.deductPoint(amount);
        return walletHistoryRecorder.recordHistory(WITHDRAWAL, wallet, amount, member);
    }

    public LinkedWallet save(final String name, final Long creatorId, final Set<Long> memberIds) {
        LinkedWallet linkedWallet = walletRepository.save(LinkedWallet.builder()
                .id(idGenerator.generate())
                .name(name)
                .build());

        Member creator = memberFetcher.fetchById(creatorId);
        linkedMemberRepository.save(LinkedMember.builder()
                .id(idGenerator.generate())
                .role(CREATOR)
                .linkedWallet(linkedWallet)
                .member(creator)
                .build());

        List<LinkedMember> linkedMembers = memberFetcher.fetchByIdIn(memberIds).stream()
                .map(tuple -> getLinkedMember(tuple, linkedWallet, PARTICIPANT))
                .toList();
        linkedMemberRepository.saveAll(linkedMembers);
        validateMemberExist(memberIds, linkedMembers);
        return linkedWallet;
    }

    private LinkedMember getLinkedMember(final Member member, final LinkedWallet linkedWallet, final Role role) {
        return LinkedMember.builder()
                .id(idGenerator.generate())
                .role(role)
                .linkedWallet(linkedWallet)
                .member(member)
                .build();
    }

    private static void validateMemberExist(final Set<Long> memberIds, final List<LinkedMember> linkedMembers) {
        if (memberIds.size() != linkedMembers.size()) {
            throw new LinkPayException(INVALID_REQUEST, "존재하지 않는 회원 아이디가 포함되어있습니다.");
        }
    }

    public void delete(LinkedWallet linkedWallet) {
        List<Long> linkedMemberIds = linkedMemberRepository.findByLinkedWalletId(linkedWallet.getId()).stream()
                .map(LinkedMember::getId)
                .toList();
        linkedMemberRepository.deleteByIds(new HashSet<>(linkedMemberIds));
        walletRepository.delete(linkedWallet);
    }
}
