package monorail.linkpay.banking.account.controller.request;

public record AccountCreateRequest(
        Long walletId,
        Long memberId
) {
}
