package monorail.linkpay.wallet.service;

import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.linkcard.service.LinkCardFetcher;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.repository.LinkedMemberRepository;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;
import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkedWalletService {

    private final LinkedWalletRepository linkedWalletRepository;
    private final LinkedWalletFetcher linkedWalletFetcher;
    private final LinkCardFetcher linkCardFetcher;
    private final MemberRepository memberRepository;
    private final MemberFetcher memberFetcher;
    private final LinkedMemberRepository linkedMemberRepository;
    private final IdGenerator idGenerator;
    private final WalletHistoryRecorder walletHistoryRecorder;
    private final LinkedMemberFetcher linkedMemberFetcher;
    private final WalletUpdater walletUpdater;

    public LinkedWalletsResponse readLinkedWallets(final long memberId, final Role role,
                                                   final Long lastId, final int size) {
        Slice<LinkedWalletDto> linkedWallets = linkedWalletRepository.findLinkedWalletsByMemberId(
                memberId,
                role,
                lastId,
                PageRequest.of(0, size)
        );
        return new LinkedWalletsResponse(linkedWallets.stream()
                .map(LinkedWalletResponse::from)
                .toList(),
                linkedWallets.hasNext());
    }

    public LinkedWalletResponse readLinkedWallet(final Long linkedWalletId) {
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        return new LinkedWalletResponse(
                linkedWalletId.toString(),
                linkedWallet.getName(),
                linkedWallet.readAmount(),
                linkedMemberRepository.countByLinkedWalletId(linkedWalletId));
    }

    @Transactional
    public Long createLinkedWallet(final long memberId, final String walletName, final Set<Long> memberIds) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet linkedWallet = LinkedWallet.builder()
                .id(idGenerator.generate())
                .name(walletName)
                .build();

        linkedWalletRepository.save(linkedWallet);
        linkedMemberRepository.save(getLinkedMember(member, linkedWallet, CREATOR));
        List<LinkedMember> linkedMembers = memberRepository.findMembersByIdIn(memberIds).stream()
                .map(tuple -> getLinkedMember(tuple, linkedWallet, PARTICIPANT))
                .toList();

        linkedMemberRepository.saveAll(linkedMembers);
        return linkedWallet.getId();
    }

    private LinkedMember getLinkedMember(final Member member, final LinkedWallet linkedWallet, final Role role) {
        return LinkedMember.builder()
                .id(idGenerator.generate())
                .role(role)
                .linkedWallet(linkedWallet)
                .member(member)
                .build();
    }

    @Transactional
    public void chargeLinkedWallet(final long linkedWalletId, final Point point, final Long memberId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet wallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        walletUpdater.chargePoint(wallet, point, member);
    }

    @Transactional
    public void deductLinkedWallet(final long linkedWalletId, final Point point, final Long memberId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet wallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        walletUpdater.deductPoint(wallet, point, member);
    }

    @Transactional
    public void deleteLinkedWallet(final Long linkedWalletId) {
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        linkCardFetcher.checkNotExistsByWalletId(linkedWalletId);
        List<Long> linkedMemberIds = linkedMemberRepository.findByLinkedWalletId(linkedWalletId).stream()
                .map(LinkedMember::getId)
                .toList();

        linkedMemberRepository.deleteByIds(new HashSet<>(linkedMemberIds));
        linkedWalletRepository.delete(linkedWallet);
    }
}
