package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.StoreCreateRequest;
import monorail.linkpay.payment.dto.TransactionInfo;
import monorail.linkpay.store.dto.StoreListResponse;
import monorail.linkpay.store.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    // todo 인증 및 인가
    @PostMapping
    public ResponseEntity<TransactionInfo> create(@RequestBody @Valid final StoreCreateRequest request) {
        Long storeId = storeService.create(request.storeName());
        return ResponseEntity.created(URI.create(String.format("/api/stores/%s", storeId))).build();
    }

    @GetMapping
    public ResponseEntity<StoreListResponse> readAll() {
        return ResponseEntity.ok(storeService.readAll());
    }
}
