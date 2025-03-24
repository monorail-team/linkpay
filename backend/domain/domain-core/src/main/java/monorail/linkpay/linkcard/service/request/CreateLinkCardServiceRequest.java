package monorail.linkpay.linkcard.service.request;

import java.time.LocalDate;
import lombok.Builder;
import monorail.linkpay.common.domain.Point;

@Builder
public record CreateLinkCardServiceRequest(
        String cardName,
        Point limitPrice,
        LocalDate expiratedAt
) {
}
