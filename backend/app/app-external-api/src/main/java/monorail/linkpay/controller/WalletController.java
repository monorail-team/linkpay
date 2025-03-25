package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.wallet.dto.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @PatchMapping("/charge")
    public ResponseEntity<Void> chargeWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final WalletPointRequest pointRequest) {
        walletService.charge(principal.memberId(), new Point(pointRequest.amount()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deduct")
    public ResponseEntity<Void> deductWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final WalletPointRequest pointRequest) {
        walletService.deduct(principal.memberId(), new Point(pointRequest.amount()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(walletService.read(principal.memberId()));
    }
}
