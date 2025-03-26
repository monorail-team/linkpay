package monorail.linkpay.controller;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.dto.WalletHistoryListResponse;
import monorail.linkpay.history.dto.WalletHistoryResponse;
import monorail.linkpay.history.service.WalletHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet-histories")
public class WalletHistoryController {

    private final WalletHistoryService walletHistoryService;

    @GetMapping("/{walletHistoryId}")
    public ResponseEntity<WalletHistoryResponse> getWalletHistory(@PathVariable final Long walletHistoryId) {
        return ResponseEntity.ok(walletHistoryService.read(walletHistoryId));
    }

    // todo 내 지갑으로 상정해서 작성된 코드, 구조 바꾼 뒤 수정하기
    @GetMapping("/list")
    public ResponseEntity<WalletHistoryListResponse> getWalletHistories(@RequestParam final Long walletId,
                                                                        @RequestParam final Long lastId,
                                                                        @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(walletHistoryService.readPage(walletId, lastId, size));
    }
}
