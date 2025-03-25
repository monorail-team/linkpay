package monorail.linkpay.controller;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.linkedwallet.service.LinkedMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/linked-wallets/{linkedWalletId}/members")
public class LinkedMemberController {

    private final LinkedMemberService linkedMemberService;

    @PostMapping
    public ResponseEntity<Void> createLinkedMember(@PathVariable final Long linkedWalletId,
                                                   @AuthenticationPrincipal final AuthPrincipal principal,
                                                   @Valid @RequestBody final LinkedMemberCreateRequest linkedMemberCreateRequest) {
        linkedMemberService.createLinkedMember(linkedWalletId, principal.memberId(),
                linkedMemberCreateRequest.memberId());
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLinkedMember(@PathVariable final Long linkedWalletId,
                                                   @RequestParam final Set<Long> linkedMemberIds,
                                                   @AuthenticationPrincipal final AuthPrincipal principal) {
        linkedMemberService.deleteLinkedMember(linkedWalletId, linkedMemberIds, principal.memberId(), now());
        return ResponseEntity.noContent().build();
    }
}
