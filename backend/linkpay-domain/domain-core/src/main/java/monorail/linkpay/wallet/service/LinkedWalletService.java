package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.member.domain.Member;
import monorail.linkpay.member.service.MemberFetcher;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkedWalletService {

    private final LinkedWalletFetcher linkedWalletFetcher;
    private final LinkedWalletValidator validator;
    private final MyWalletFetcher myWalletFetcher;
    private final WalletUpdater walletUpdater;
    private final MemberFetcher memberFetcher;

    public LinkedWalletsResponse readLinkedWallets(final long memberId, final Role role,
                                                   final Long lastId, final int size) {
        Slice<LinkedWalletDto> linkedWallets = linkedWalletFetcher.fetchPage(memberId, role, lastId, size);
        return new LinkedWalletsResponse(linkedWallets.stream()
                .map(LinkedWalletResponse::from)
                .toList(),
                linkedWallets.hasNext());
    }

    public LinkedWalletResponse readLinkedWallet(final Long linkedWalletId, final long memberId) {
        validator.validateRead(linkedWalletId, memberId);
        return linkedWalletFetcher.fetchWithCountById(linkedWalletId);
    }

    @Transactional
    public Long createLinkedWallet(final long memberId, final String walletName, final Set<Long> memberIds) {
        validator.validateCreate(memberId, memberIds);
        LinkedWallet linkedWallet = walletUpdater.save(walletName, memberId, memberIds);
        return linkedWallet.getId();
    }

    @Transactional
    public void chargeLinkedWallet(final Long linkedWalletId, final Point point, final long memberId) {
        validator.validateCharge(linkedWalletId, memberId);
        Member member = memberFetcher.fetchById(memberId);
        LinkedWallet wallet = linkedWalletFetcher.fetchByIdForUpdate(linkedWalletId);
        walletUpdater.chargePoint(wallet, point, member);
    }

    @Transactional
    public void deleteLinkedWallet(final Long linkedWalletId, final long memberId) {
        validator.validateDelete(linkedWalletId, memberId);
        LinkedWallet linkedWallet = linkedWalletFetcher.fetchById(linkedWalletId);
        MyWallet myWallet = myWalletFetcher.fetchByMemberIdForUpdate(memberId);
        walletUpdater.chargePoint(myWallet, linkedWallet.getPoint(), memberFetcher.fetchById(memberId));
        walletUpdater.delete(linkedWallet);
    }
}
