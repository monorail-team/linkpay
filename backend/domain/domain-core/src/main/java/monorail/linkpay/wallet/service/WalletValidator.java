package monorail.linkpay.wallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.wallet.domain.Wallet;

import static monorail.linkpay.exception.ExceptionCode.INVALID_DEDUCT_AMOUNT;

@SupportLayer
@RequiredArgsConstructor
public class WalletValidator {

    public void validateDeducting(final Wallet wallet, final Long amount) {
        if (wallet.getAmount() < amount) {
            throw new LinkPayException(INVALID_DEDUCT_AMOUNT, "잔액이 부족합니다.");
        }
    }
}
