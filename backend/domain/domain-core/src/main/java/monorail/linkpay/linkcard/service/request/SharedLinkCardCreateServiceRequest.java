package monorail.linkpay.linkcard.service.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;

@Builder
public record SharedLinkCardCreateServiceRequest(
        String cardName,
        Point limitPrice,
        LocalDate expiratedAt,
        List<Long> memberIds,
        Long linkedWalletId
) {
}
