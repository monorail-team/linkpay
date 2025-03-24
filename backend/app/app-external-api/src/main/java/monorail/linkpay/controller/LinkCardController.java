package monorail.linkpay.controller;

import static org.springframework.http.HttpStatus.CREATED;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.dto.LinkCardsResponse;
import monorail.linkpay.linkcard.service.LinkCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
                                               @Valid @RequestBody final LinkCardCreateRequest createLinkCardRequest) {
        linkCardService.create(principal.memberId(), createLinkCardRequest.toServiceRequest());
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping
    public ResponseEntity<LinkCardsResponse> getLinkCards(@AuthenticationPrincipal final AuthPrincipal principal,
                                                          @RequestParam(required = false) final Long lastId,
                                                          @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(linkCardService.read(principal.memberId(), lastId, size));
    }

    @GetMapping("/unregister")
    public ResponseEntity<LinkCardsResponse> unregisterLinkCard(@AuthenticationPrincipal final AuthPrincipal principal,
                                                                @RequestParam(required = false) final Long lastId,
                                                                @RequestParam(defaultValue = "10") final int size) {
        return ResponseEntity.ok(linkCardService.readUnregister(principal.memberId(), lastId, size));
    }
}
