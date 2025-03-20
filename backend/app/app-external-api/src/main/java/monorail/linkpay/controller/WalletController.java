package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.ChargeRequest;
import monorail.linkpay.controller.request.DeductRequest;
import monorail.linkpay.wallet.service.WalletHistoryListResponse;
import monorail.linkpay.wallet.service.WalletHistoryService;
import monorail.linkpay.wallet.service.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets/{walletId}")
public class WalletController {

    private final WalletService walletService;
    private final WalletHistoryService walletHistoryService;

    @PatchMapping("/charge")
    public ResponseEntity<Void> chargeWallet(@PathVariable final Long walletId,
                                             @Valid @RequestBody final ChargeRequest chargeRequest) {
        walletService.charge(walletId, chargeRequest.amount());
        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/deduct")
    public ResponseEntity<Void> deductWallet(@PathVariable final Long walletId,
                                             @Valid @RequestBody final DeductRequest deductRequest) {
        walletService.deduct(walletId, deductRequest.amount());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@PathVariable final Long walletId) {
        return ResponseEntity.ok(walletService.read(walletId));
    }

    @GetMapping("/history")
    public ResponseEntity<WalletHistoryListResponse> getWalletHistories(@RequestParam final Long walletId,
                                                                        @RequestParam final Long lastId,
                                                                        @RequestParam final int size) {
        return ResponseEntity.ok(walletHistoryService.readPage(walletId, lastId, size));
    }
}
