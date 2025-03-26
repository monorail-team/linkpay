package monorail.linkpay.wallet.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.Wallet;
import monorail.linkpay.wallet.repository.MyWalletRepository;

@SupportLayer
@RequiredArgsConstructor
public class WalletFetcher {

    private final MyWalletRepository myWalletRepository;

    public Wallet fetchById(final Long id) {
        return myWalletRepository.findById(id)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }

    // 쓰기 용도: 락 걸고 조회
    public Wallet fetchByMemberIdForUpdate(Long memberId) {
        return myWalletRepository.findByMemberIdForUpdate(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }

    // 읽기 전용
    public Wallet fetchByMemberId(final Long memberId) {
        return myWalletRepository.findByMemberId(memberId)
                .orElseThrow(() ->
                        new LinkPayException(NOT_FOUND_RESOURCE, "요청한 멤버 아이디에 해당하는 지갑이 존재하지 않습니다."));
    }
}
