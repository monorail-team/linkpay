package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.auth.AuthPrincipal;
import monorail.linkpay.controller.request.LinkCardCreateRequest;
import monorail.linkpay.linkcard.service.LinkCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
