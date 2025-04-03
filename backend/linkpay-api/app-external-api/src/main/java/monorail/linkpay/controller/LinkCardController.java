package monorail.linkpay.controller;

import static java.time.LocalDateTime.now;
import static monorail.linkpay.linkcard.domain.CardState.getCardState;
import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.controller.request.LinkCardRegistRequest;
import monorail.linkpay.controller.request.SharedLinkCardCreateRequest;
import monorail.linkpay.linkcard.dto.LinkCardDetailResponse;
import monorail.linkpay.linkcard.dto.LinkCardHistoriesResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
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
@RequestMapping("/api/cards")
public class LinkCardController {

    private final LinkCardService linkCardService;

    @PostMapping
    public ResponseEntity<Void> createLinkCard(@AuthenticationPrincipal final AuthPrincipal principal,
                                               @Valid @RequestBody final LinkCardCreateRequest linkCardCreateRequest) {
        linkCardService.create(principal.memberId(), linkCardCreateRequest.toServiceRequest());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/shared")
    public ResponseEntity<Void> createSharedLinkCard(
            @Valid @RequestBody final SharedLinkCardCreateRequest sharedLinkCardCreateRequest,
            @AuthenticationPrincipal final AuthPrincipal principal) {
        linkCardService.createShared(sharedLinkCardCreateRequest.toServiceRequest(), principal.memberId());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<LinkCardsResponse> getLinkCards(@RequestParam(required = false) final Long lastId,
                                                          @RequestParam(defaultValue = "10") final int size,
                                                          @RequestParam final String type,
                                                          @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(linkCardService.read(principal.memberId(), lastId, size, type));
    }

    @GetMapping("/{state}")
    public ResponseEntity<LinkCardsResponse> getLinkCardsByState(@PathVariable final String state,
                                                                 @RequestParam(required = false) final Long lastId,
                                                                 @RequestParam(defaultValue = "10") final int size,
                                                                 @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(linkCardService.readByState(principal.memberId(), lastId, size, getCardState(state),
                now()));
    }

    @GetMapping("/detail")
    public ResponseEntity<LinkCardDetailResponse> getLinkCardDetails(@RequestParam final Long linkCardId,
                                                                     @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(linkCardService.getLinkCardDetails(principal.memberId(), linkCardId));

    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activateLinkCard(
            @Valid @RequestBody final LinkCardRegistRequest linkCardRegistRequest,
            @AuthenticationPrincipal final AuthPrincipal principal) {
        linkCardService.activateLinkCard(linkCardRegistRequest.linkCardIds().stream().map(Long::parseLong).toList(),
                principal.memberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLinkCard(@RequestParam final Long linkCardId,
                                               @AuthenticationPrincipal final AuthPrincipal principal) {
        linkCardService.deleteLinkCard(linkCardId, principal.memberId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/card-histories/{linkCardId}")
    public ResponseEntity<LinkCardHistoriesResponse> getLinkCardHistories(@PathVariable final Long linkCardId,
                                                                          @RequestParam(required = false) final Long lastId,
                                                                          @RequestParam(defaultValue = "10") final int size,
                                                                          @AuthenticationPrincipal final AuthPrincipal principal) {
        return ResponseEntity.ok(linkCardService.getLinkCardHistories(principal.memberId(), lastId, size, linkCardId));
    }
}
