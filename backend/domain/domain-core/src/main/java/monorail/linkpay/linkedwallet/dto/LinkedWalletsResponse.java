package monorail.linkpay.linkedwallet.dto;

import java.util.List;

public record LinkedWalletsResponse(
        List<LinkedWalletResponse> linkedWallets,
        boolean hasNext
) {
}
