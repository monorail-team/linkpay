package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
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

import java.util.List;
import java.util.Set;

import static monorail.linkpay.wallet.domain.Role.CREATOR;
import static monorail.linkpay.wallet.domain.Role.PARTICIPANT;

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
    private final WalletUpdater walletUpdater;

    public LinkedWalletsResponse readLinkedWallets(final long memberId, final Long lastId, final int size) {
        Slice<LinkedWalletDto> linkedWalletDtos = linkedMemberRepository.findLinkedWalletDtosByMemberId(
                memberId,
                lastId,
                PageRequest.of(0, size)
        );
        return new LinkedWalletsResponse(linkedWalletDtos.stream()
                .map(LinkedWalletResponse::from)
                .toList(),
                linkedWalletDtos.hasNext());
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

    private LinkedMember getLinkedMember(final Member member, final LinkedWallet linkedWallet, final Role role) {
        return LinkedMember.builder()
                .id(idGenerator.generate())
                .role(role)
                .linkedWallet(linkedWallet)
                .member(member)
                .build();
    }
}
