package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.controller.request.StoreTransactionRequest;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.store.service.StoreTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/transactions")
public class StoreTransactionController {

    private final StoreTransactionService storeTransactionService;

    // todo 인증 및 인가
    @PostMapping
    public ResponseEntity<TransactionResponse.Flat> create(@PathVariable(name = "storeId") final Long storeId,
                                                      @RequestBody final StoreTransactionRequest request) {
        TransactionInfo txInfo = storeTransactionService.create(storeId, new Point(request.amount()));
        return ResponseEntity.ok(TransactionResponse.from(txInfo).flat());
    }
}
