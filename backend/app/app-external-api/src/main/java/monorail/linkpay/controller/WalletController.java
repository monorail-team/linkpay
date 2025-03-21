package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.controller.request.PointRequest;
import monorail.linkpay.wallet.dto.WalletHistoryListResponse;
import monorail.linkpay.wallet.service.WalletHistoryService;
import monorail.linkpay.wallet.dto.WalletResponse;
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
                                             @Valid @RequestBody final PointRequest pointRequest) {
        walletService.charge(principal.memberId(), new Point(pointRequest.amount()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deduct")
    public ResponseEntity<Void> deductWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final PointRequest pointRequest) {
        walletService.deduct(principal.memberId(), new Point(pointRequest.amount()));
        return ResponseEntity.noContent().build();
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
