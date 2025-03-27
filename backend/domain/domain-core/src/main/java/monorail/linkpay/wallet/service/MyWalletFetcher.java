package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.MyWallet;
import monorail.linkpay.wallet.repository.MyWalletRepository;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

@SupportLayer
@RequiredArgsConstructor
public class MyWalletFetcher {

    private final MyWalletRepository myWalletRepository;

    public MyWallet fetchById(final Long id) {
        return myWalletRepository.findById(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }

    public MyWallet fetchByMemberIdForUpdate(final Long memberId) {
        return myWalletRepository.findByMemberIdForUpdate(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }

    public MyWallet fetchByMemberId(final Long memberId) {
        return myWalletRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }
}
