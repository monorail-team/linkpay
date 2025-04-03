package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;
import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@SupportLayer
@RequiredArgsConstructor
public class LinkedWalletFetcher {

    private final LinkedWalletRepository linkedWalletRepository;
    private final LinkedMemberFetcher linkedMemberFetcher;

    public void checkExistsById(final Long id) {
        if (!linkedWalletRepository.existsById(id)) {
            throw new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크지갑이 존재하지 않습니다.");
        }
    }

    public LinkedWallet fetchById(final Long id) {
        return linkedWalletRepository.findById(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크지갑이 존재하지 않습니다."));
    }

    public LinkedWalletResponse fetchWithCountById(final Long id) {
        LinkedWallet linkedWallet = fetchById(id);
        return new LinkedWalletResponse(
                id.toString(),
                linkedWallet.getName(),
                linkedWallet.readAmount(),
                linkedMemberFetcher.fetchCountByLinkedWalletId(id));
    }

    public LinkedWallet fetchByIdForUpdate(final Long id) {
        return linkedWalletRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크지갑이 존재하지 않습니다."));
    }

    public Slice<LinkedWalletDto> fetchPage(final long memberId,
                                            final Role role,
                                            final Long lastId,
                                            final int size) {
         return linkedWalletRepository.findLinkedWalletsByMemberId(
                memberId,
                role,
                lastId,
                PageRequest.of(0, size)
        );
    }
}
