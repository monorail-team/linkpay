package monorail.linkpay.payment.service;

import static monorail.linkpay.exception.ExceptionCode.NOT_FOUND_RESOURCE;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.annotation.SupportLayer;
import monorail.linkpay.exception.LinkPayException;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.payment.repository.PaymentRepository;

@SupportLayer
@RequiredArgsConstructor
public class PaymentFetcher {

    private final PaymentRepository paymentRepository;

    public Payment fetchById(final Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new LinkPayException(NOT_FOUND_RESOURCE, "아이디에 해당하는 내역이 존재하지 않습니다."));
    }

    public List<Payment> fetchByWalletHistoryIdIn(Set<Long> walletHistoryIds) {
        return paymentRepository.findAllByWalletHistoryIdIn(walletHistoryIds);
    }
}
