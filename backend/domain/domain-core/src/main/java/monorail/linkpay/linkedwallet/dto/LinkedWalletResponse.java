package monorail.linkpay.linkedwallet.dto;

import monorail.linkpay.linkedwallet.repository.dto.LinkedWalletDto;

public record LinkedWalletResponse(
        Long linkedWalletId,
        String linkedWalletName,
        Long amount
) {
    public static LinkedWalletResponse from(LinkedWalletDto dto) {
        return new LinkedWalletResponse(
                dto.getLinkedWalletId(),
                dto.getLinkedWalletName(),
                dto.getAmount()
        );
    }
}
