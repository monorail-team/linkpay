package monorail.linkpay.banking.account.controller;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.banking.account.controller.request.AccountCreateRequest;
import monorail.linkpay.banking.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banking-api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody final AccountCreateRequest accountCreateRequest) {
        accountService.createAccount(accountCreateRequest.walletId(), accountCreateRequest.memberId());
        return ResponseEntity.status(CREATED).build();
    }
}
