package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.store.dto.TransactionResponse;
import monorail.linkpay.store.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores/{storeId}/transactions")
public class StoreTransactionController {

    private final TransactionService transactionService;

    // todo 인증 및 인가
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@PathVariable(name = "storeId") final Long storeId,
                                                      @RequestParam final long amount) {
        var response = transactionService.create(storeId, amount);
        return ResponseEntity.ok(response);
    }
}
