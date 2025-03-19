package monorail.linkpay.linkedwallet.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.linkedwallet.repository.LinkedWalletRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkedWalletService {

    private final LinkedWalletRepository linkedWalletRepository;
}
