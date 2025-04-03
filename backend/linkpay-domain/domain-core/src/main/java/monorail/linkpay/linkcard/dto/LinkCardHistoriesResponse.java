package monorail.linkpay.linkcard.dto;

import java.util.List;

public record LinkCardHistoriesResponse(
        List<LinkCardHistoryResponse> linkCardHistories,
        boolean hasNext
) {
}
