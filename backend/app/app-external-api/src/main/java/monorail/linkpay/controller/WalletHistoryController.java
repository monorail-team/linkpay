package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.wallet.dto.WalletHistoryResponse;
import monorail.linkpay.wallet.service.WalletHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet-history")
public class WalletHistoryController {

    private final WalletHistoryService walletHistoryService;

    @GetMapping("/{walletHistoryId}")
    public ResponseEntity<WalletHistoryResponse> getWalletHistory(@PathVariable final Long walletHistoryId) {
        return ResponseEntity.ok(walletHistoryService.read(walletHistoryId));
    }
}
