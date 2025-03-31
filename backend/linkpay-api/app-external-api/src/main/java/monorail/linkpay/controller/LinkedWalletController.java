package monorail.linkpay.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.controller.request.WalletPointRequest;
import monorail.linkpay.wallet.domain.Role;
import monorail.linkpay.wallet.dto.LinkedWalletResponse;
import monorail.linkpay.wallet.dto.LinkedWalletsResponse;
import monorail.linkpay.wallet.service.LinkedWalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/linked-wallets")
public class LinkedWalletController {

    private final LinkedWalletService linkedWalletService;

    @GetMapping
    public ResponseEntity<LinkedWalletsResponse> getLinkedWallets(@RequestParam(required = false) final Long lastId,
                                                                  @RequestParam(defaultValue = "10") final int size,
                                                                  @RequestParam final Role role,
                                                                  @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(linkedWalletService.readLinkedWallets(principal.memberId(), role, lastId, size));
    }

    @GetMapping("/{linkedWalletId}")
    public ResponseEntity<LinkedWalletResponse> getLinkedWallet(@PathVariable final Long linkedWalletId) {
        return ResponseEntity.ok(linkedWalletService.readLinkedWallet(linkedWalletId));
    }

    @PostMapping
    public ResponseEntity<Void> createLinkedWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                                   @Valid @RequestBody final LinkedWalletCreateRequest linkedWalletCreateRequest) {
        Long linkedWalletId = linkedWalletService.createLinkedWallet(
                principal.memberId(),
                linkedWalletCreateRequest.walletName(),
                linkedWalletCreateRequest.memberIds());

        return ResponseEntity.created(URI.create("/api/linked-wallets/" + linkedWalletId)).build();
    }

    @PatchMapping("/charge/{linkedWalletId}")
    public ResponseEntity<Void> chargeLinkedWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                                   @PathVariable final Long linkedWalletId,
                                                   @Valid @RequestBody final WalletPointRequest walletPointRequest) {
        linkedWalletService.chargeLinkedWallet(linkedWalletId, new Point(walletPointRequest.amount()),
                principal.memberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{linkedWalletId}")
    public ResponseEntity<Void> deleteLinkedWallet(@PathVariable final Long linkedWalletId) {
        linkedWalletService.deleteLinkedWallet(linkedWalletId);
        return ResponseEntity.noContent().build();
    }
}
