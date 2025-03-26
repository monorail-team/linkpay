package monorail.linkpay.wallet.service;

import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.common.domain.TransactionType;
import monorail.linkpay.history.service.WalletHistoryRecorder;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
import monorail.linkpay.wallet.domain.LinkedMember;
import monorail.linkpay.wallet.domain.LinkedWallet;
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
    private final MemberRepository memberRepository;
    private final MemberFetcher memberFetcher;
    private final LinkedMemberRepository linkedMemberRepository;
    private final IdGenerator idGenerator;
    private final WalletHistoryRecorder walletHistoryRecorder;

    public LinkedWalletsResponse readLinkedWallets(final long memberId, final Long lastId, final int size) {
        Slice<LinkedWalletDto> linkedWalletDtos = linkedMemberRepository.findByMemberId(memberId, lastId,
                PageRequest.of(0, size));

        return new LinkedWalletsResponse(linkedWalletDtos.stream()
                .map(LinkedWalletResponse::from)
                .toList(),
                linkedWalletDtos.hasNext());
    }

    public LinkedWalletResponse readLinkedWallet(final Long linkedWalletId) {
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        return new LinkedWalletResponse(
                linkedWalletId,
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

        linkedMemberRepository.save(LinkedMember.builder()
                .id(idGenerator.generate())
                .role(CREATOR)
                .linkedWallet(linkedWallet)
                .member(member).build());

        memberRepository.findMembersByIdIn(memberIds).forEach(tuple ->
                linkedMemberRepository.save(LinkedMember.builder()
                        .id(idGenerator.generate())
                        .role(PARTICIPANT)
                        .linkedWallet(linkedWallet)
                        .member(member).build())
        );

        linkedWalletRepository.save(linkedWallet);
        return linkedWallet.getId();
    }

    @Transactional
    public void chargeLinkedWallet(final long linkedWalletId, final Point point, final Long memberId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        linkedWallet.chargePoint(point);
        walletHistoryRecorder.recordHistory(TransactionType.DEPOSIT, linkedWallet, point, member);
    }

    @Transactional
    public void deductLinkedWallet(final long linkedWalletId, final Point point, final Long memberId) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        linkedWallet.deductPoint(point);
        walletHistoryRecorder.recordHistory(TransactionType.DEPOSIT, linkedWallet, point, member);
    }
}
