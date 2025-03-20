package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.ChargeRequest;
import monorail.linkpay.controller.request.DeductRequest;
import monorail.linkpay.wallet.service.WalletHistoryListResponse;
import monorail.linkpay.wallet.service.WalletHistoryService;
import monorail.linkpay.wallet.service.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletHistoryService walletHistoryService;

    @PatchMapping("/charge")
    public ResponseEntity<Void> chargeWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final ChargeRequest chargeRequest) {
        walletService.charge(principal.memberId(), chargeRequest.amount());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deduct")
    public ResponseEntity<Void> deductWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final DeductRequest deductRequest) {
        walletService.deduct(principal.memberId(), deductRequest.amount());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(walletService.read(principal.memberId()));
    }

    @GetMapping("/history")
    public ResponseEntity<WalletHistoryListResponse> getWalletHistories(@RequestParam final Long walletId,
                                                                        @RequestParam final Long lastId,
                                                                        @RequestParam final int size) {
        return ResponseEntity.ok(walletHistoryService.readPage(walletId, lastId, size));
    }
}
