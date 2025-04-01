package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.StoreTransactionRequest;
import monorail.linkpay.store.dto.TransactionSign;
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
    public ResponseEntity<TransactionSign> create(@PathVariable(name = "storeId") final Long storeId,
                                                  @RequestBody final StoreTransactionRequest request) {
        var response = storeTransactionService.create(storeId, request.amount());
        return ResponseEntity.ok(response);
    }
}
