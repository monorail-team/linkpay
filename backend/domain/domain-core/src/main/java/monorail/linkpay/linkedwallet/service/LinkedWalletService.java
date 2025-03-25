package monorail.linkpay.linkedwallet.service;

import static monorail.linkpay.linkedwallet.domain.Role.CREATOR;
import static monorail.linkpay.linkedwallet.domain.Role.PARTICIPANT;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkedwallet.domain.LinkedMember;
import monorail.linkpay.linkedwallet.domain.LinkedWallet;
import monorail.linkpay.linkedwallet.dto.LinkedWalletResponse;
import monorail.linkpay.linkedwallet.dto.LinkedWalletsResponse;
import monorail.linkpay.linkedwallet.repository.LinkedMemberRepository;
import monorail.linkpay.linkedwallet.repository.LinkedWalletRepository;
import monorail.linkpay.linkedwallet.repository.dto.LinkedWalletDto;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.repository.MemberRepository;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.util.id.IdGenerator;
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

    public LinkedWalletsResponse readLinkedWallets(final long memberId, final Long lastId, final int size) {
        Slice<LinkedWalletDto> linkedWalletDtos = linkedMemberRepository.findByMemberId(memberId, lastId,
                PageRequest.of(0, size));

        return new LinkedWalletsResponse(linkedWalletDtos.stream()
                .map(LinkedWalletResponse::from)
                .toList(),
                linkedWalletDtos.hasNext());
    }

    @Transactional
    public Long createLinkedWallet(final long memberId, final String walletName, final Set<Long> memberIds) {
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet linkedWallet = LinkedWallet.builder()
                .id(idGenerator.generate())
                .name(walletName)
                .build();

        linkedWallet.registerLinkedMember(LinkedMember.of(member, idGenerator.generate(), CREATOR));
        memberRepository.findMembersByIdIn(memberIds).forEach(tuple -> linkedWallet.registerLinkedMember(
                LinkedMember.of(tuple, idGenerator.generate(), PARTICIPANT)));

        linkedWalletRepository.save(linkedWallet);
        return linkedWallet.getId();
    }

    @Transactional
    public void chargeLinkedWallet(final long linkedWalletId, final Point point) {
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        linkedWallet.chargePoint(point);
    }

    @Transactional
    public void deductLinkedWallet(final long linkedWalletId, final Point point) {
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        linkedWallet.deductPoint(point);
    }
}
