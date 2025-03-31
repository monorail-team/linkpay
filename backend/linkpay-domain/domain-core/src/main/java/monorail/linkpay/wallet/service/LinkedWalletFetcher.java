package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.LinkedWallet;
import monorail.linkpay.wallet.repository.LinkedWalletRepository;

@SupportLayer
@RequiredArgsConstructor
public class LinkedWalletFetcher {

    private final LinkedWalletRepository linkedWalletRepository;

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

    public LinkedWallet fetchByIdForUpdate(final Long id) {
        return linkedWalletRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 링크지갑이 존재하지 않습니다."));
    }
}
