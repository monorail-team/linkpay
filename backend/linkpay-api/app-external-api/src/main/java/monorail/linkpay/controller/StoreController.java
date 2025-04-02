package monorail.linkpay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.controller.request.StoreCreateRequest;
import monorail.linkpay.store.dto.TransactionResponse;
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
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid final StoreCreateRequest request) {
        Long storeId = storeService.create(request.storeName());
        return ResponseEntity.created(URI.create(String.format("/api/stores/%s", storeId))).build();
    }
}
