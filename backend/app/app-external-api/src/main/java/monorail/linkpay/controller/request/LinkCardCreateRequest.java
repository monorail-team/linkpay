package monorail.linkpay.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.linkcard.service.request.CreateLinkCardServiceRequest;

public record LinkCardCreateRequest(

        @NotEmpty(message = "카드 이름을 입력해주세요.")
        @Size(max = 50, message = "카드 이름은 50자를 넘을 수 없습니다.")
        String cardName,

        @Positive(message = "한도 금액은 양수여야 합니다.")
        @Max(value = 10000000, message = "한도 금액은 최대 1,000만원까지 가능합니다.")
        long limitPrice,

        @NotNull(message = "카드 만료일을 입력해주세요.")
        LocalDate expiredAt
) {
    public CreateLinkCardServiceRequest toServiceRequest() {
        return CreateLinkCardServiceRequest.builder()
                .cardName(cardName)
                .limitPrice(new Point(limitPrice))
                .expiratedAt(expiredAt)
                .build();
    }
}
