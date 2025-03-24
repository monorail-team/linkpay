package monorail.linkpay.linkcard.dto;

import java.util.List;

public record LinkCardsResponse(
        List<LinkCardResponse> linkCards,
        Boolean hasNext
) {
}
