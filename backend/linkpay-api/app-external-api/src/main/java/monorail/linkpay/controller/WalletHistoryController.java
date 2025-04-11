package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.facade.WalletHistoryFacade;
import monorail.linkpay.facade.dto.WalletHistoryListResponse;
import monorail.linkpay.facade.dto.WalletHistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet-histories")
public class WalletHistoryController {

    private final WalletHistoryFacade walletHistoryFacade;

    @GetMapping("/{walletHistoryId}")
    public ResponseEntity<WalletHistoryResponse> getWalletHistory(@PathVariable final Long walletHistoryId,
                                                                  @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(walletHistoryFacade.readWalletHistory(walletHistoryId, principal.memberId()));
    }

    @GetMapping("/my-wallet")
    public ResponseEntity<WalletHistoryListResponse> getMyWalletHistories(
            @AuthenticationPrincipal final AuthPrincipal principal,
            @RequestParam(required = false) final Long lastId,
            @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(walletHistoryFacade.readMyWalletHistoryPage(principal.memberId(), lastId, size));
    }

    @GetMapping("/linked-wallet")
    public ResponseEntity<WalletHistoryListResponse> getLinkedWalletHistories(@RequestParam final Long walletId,
                                                                              @RequestParam(required = false) final Long lastId,
                                                                              @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(walletHistoryFacade.readLinkedWalletHistoryPage(walletId, lastId, size));
    }
}
