package monorail.linkpay.wallet.dto;

public record AccountCreateRequest(
        Long walletId,
        Long memberId
) {
}
