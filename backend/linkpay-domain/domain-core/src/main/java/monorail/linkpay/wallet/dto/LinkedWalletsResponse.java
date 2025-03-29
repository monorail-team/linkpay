package monorail.linkpay.wallet.dto;

import java.util.List;

public record LinkedWalletsResponse(
        List<LinkedWalletResponse> linkedWallets,
        boolean hasNext
) {
}
