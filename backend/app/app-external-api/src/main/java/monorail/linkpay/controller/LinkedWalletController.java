package monorail.linkpay.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkedWalletCreateRequest;
import monorail.linkpay.linkedwallet.service.LinkedWalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/linked-wallets")
public class LinkedWalletController {

    private final LinkedWalletService linkedWalletService;

    @PostMapping
    public ResponseEntity<Void> createLinkedWallet(@AuthenticationPrincipal final AuthPrincipal principal,
                                                   @Valid @RequestBody final LinkedWalletCreateRequest linkedWalletCreateRequest) {
        Long linkedWalletId = linkedWalletService.createLinkedWallet(
                principal.memberId(),
                linkedWalletCreateRequest.walletName(),
                linkedWalletCreateRequest.memberIds());

        return ResponseEntity.created(URI.create("/api/linked-wallets/" + linkedWalletId)).build();
    }
}
