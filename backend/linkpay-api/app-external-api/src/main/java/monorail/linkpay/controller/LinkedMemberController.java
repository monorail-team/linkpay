package monorail.linkpay.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkedMemberCreateRequest;
import monorail.linkpay.wallet.dto.LinkedMembersResponse;
import monorail.linkpay.wallet.service.LinkedMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<LinkedMembersResponse> getLinkedMembers(@PathVariable final Long linkedWalletId,
                                                                  @RequestParam(required = false) final Long lastId,
                                                                  @RequestParam(defaultValue = "10") final int size,
                                                                  @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(
                linkedMemberService.getLinkedMembers(linkedWalletId, principal.memberId(), lastId, size));
    }

    @PostMapping
    public ResponseEntity<Void> createLinkedMember(@PathVariable final Long linkedWalletId,
                                                   @AuthenticationPrincipal final AuthPrincipal principal,
                                                   @Valid @RequestBody final LinkedMemberCreateRequest linkedMemberCreateRequest) {
        linkedMemberService.createLinkedMember(linkedWalletId, principal.memberId(),
                Long.parseLong(linkedMemberCreateRequest.memberId()));
        return ResponseEntity.status(CREATED).build();
    }

    @DeleteMapping("/{linkedMemberId}")
    public ResponseEntity<Void> deleteLinkedMember(@PathVariable final Long linkedWalletId,
                                                   @PathVariable final Long linkedMemberId,
                                                   @AuthenticationPrincipal final AuthPrincipal principal) {
        linkedMemberService.deleteLinkedMember(linkedWalletId, linkedMemberId, principal.memberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLinkedMembers(@PathVariable final Long linkedWalletId,
                                                    @RequestParam final Set<Long> linkedMemberIds,
                                                    @AuthenticationPrincipal final AuthPrincipal principal) {
        linkedMemberService.deleteLinkedMembers(linkedWalletId, linkedMemberIds, principal.memberId());
        return ResponseEntity.noContent().build();
    }
}
