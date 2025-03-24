package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.domain.LinkCard;
import monorail.linkpay.linkcard.dto.LinkCardResponse;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class LinkCardController {

    private final LinkCardService linkCardService;

    @PostMapping
    public ResponseEntity<Void> createLinkCard(@AuthenticationPrincipal final AuthPrincipal principal,
                                               @Valid @RequestBody final LinkCardCreateRequest createLinkCardRequest) {
        linkCardService.create(principal.memberId(), createLinkCardRequest.toServiceRequest());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<LinkCardsResponse> getLinkCards(@AuthenticationPrincipal final AuthPrincipal principal, @RequestParam final Long lastId,
                                                          @RequestParam final int size) {
        return ResponseEntity.ok(linkCardService.read(principal.memberId(), lastId, size));
    }

    @GetMapping("/unregister")
    public ResponseEntity<LinkCardsResponse> unregisterLinkCard(@AuthenticationPrincipal final AuthPrincipal principal, @RequestParam final Long lastId,
                                                                @RequestParam final int size) {
        return ResponseEntity.ok(linkCardService.readUnregister(principal.memberId(), lastId, size));
    }
}
