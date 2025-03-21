package monorail.linkpay.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;

import java.time.LocalDate;

public record LinkCardCreateRequest(

        @NotNull
        String cardName,

        @Positive
        long limitPrice,

        @NotNull
        LocalDate expiratedAt
) {
    public CreateLinkCardServiceRequest toServiceRequest() {
        return CreateLinkCardServiceRequest.builder()
                .cardName(cardName)
                .limitPrice(new Point(limitPrice))
                .expiratedAt(expiratedAt)
                .build();
    }
}
