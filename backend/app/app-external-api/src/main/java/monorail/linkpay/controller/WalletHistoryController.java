package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.history.service.WalletHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet-histories")
public class WalletHistoryController {

    private final WalletHistoryService walletHistoryService;

    @GetMapping("/{walletHistoryId}")
    public ResponseEntity<WalletHistoryResponse> getWalletHistory(@PathVariable final Long walletHistoryId) {
        return ResponseEntity.ok(walletHistoryService.readWalletHistory(walletHistoryId));
    }

    @GetMapping("/my-wallet")
    public ResponseEntity<WalletHistoryListResponse> getMyWalletHistories(
            @AuthenticationPrincipal final AuthPrincipal principal,
            @RequestParam(required = false) final Long lastId,
            @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(walletHistoryService.readMyWalletHistoryPage(principal.memberId(), lastId, size));
    }

    @GetMapping("linked-wallet")
    public ResponseEntity<WalletHistoryListResponse> getLInkedWalletHistories(
            @RequestParam final Long walletId,
            @RequestParam(required = false) final Long lastId,
            @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(walletHistoryService.readLinkedWalletHistoryPage(walletId, lastId, size));
    }
}
