package monorail.linkpay.linkcard.service.request;

import lombok.Builder;
import monorail.linkpay.common.domain.Point;

import java.time.LocalDate;

@Builder
public record CreateLinkCardServiceRequest(
        String cardName,
        Point limitPrice,
        LocalDate expiratedAt
) {
}
