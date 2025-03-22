package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.WalletRepository;

@SupportLayer
@RequiredArgsConstructor
public class WalletFetcher {

    private final WalletRepository walletRepository;

    public void checkExistsById(final Long id) {
        if (!walletRepository.existsById(id)) {
            throw new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 지갑이 존재하지 않습니다.");
        }
    }

    public Wallet fetchById(final Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }

    public Wallet fetchByMemberId(final Long memberId) {
        return walletRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }
}
