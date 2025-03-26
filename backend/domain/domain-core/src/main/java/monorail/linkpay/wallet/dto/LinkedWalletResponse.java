package monorail.linkpay.wallet.dto;

import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;

public record LinkedWalletResponse(
        Long linkedWalletId,
        String linkedWalletName,
        Long amount,
        int participantCount
) {
    public static LinkedWalletResponse from(LinkedWalletDto dto) {
        return new LinkedWalletResponse(
                dto.getLinkedWalletId(),
                dto.getLinkedWalletName(),
                dto.getAmount(),
                dto.getParticipantCount()
        );
    }
}
