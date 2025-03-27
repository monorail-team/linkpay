package monorail.linkpay.wallet.dto;

import monorail.linkpay.wallet.repository.dto.LinkedWalletDto;

public record LinkedWalletResponse(
        String linkedWalletId,
        String linkedWalletName,
        Long amount,
        int participantCount
) {
    public static LinkedWalletResponse from(final LinkedWalletDto dto) {
        return new LinkedWalletResponse(
                dto.getLinkedWalletId().toString(),
                dto.getLinkedWalletName(),
                dto.getAmount(),
                dto.getParticipantCount()
        );
    }
}
