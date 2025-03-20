package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.ChargeRequest;
import monorail.linkpay.wallet.service.WalletResponse;
import monorail.linkpay.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @PatchMapping("/charge")
    public ResponseEntity<Void> chargeWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                             @Valid @RequestBody final ChargeRequest chargeRequest) {
        walletService.charge(principal.memberId(), chargeRequest.amount());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(@AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(walletService.read(principal.memberId()));
    }
}
