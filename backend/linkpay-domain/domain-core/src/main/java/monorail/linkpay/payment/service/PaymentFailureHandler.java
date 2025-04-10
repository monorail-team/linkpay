package monorail.linkpay.payment.service;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.payment.dto.PaymentInfo;
import monorail.linkpay.payment.dto.TransactionInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PaymentFailureHandler {

    private final PaymentNotifier paymentNotifier;

    @AfterThrowing(
            pointcut = "execution(* monorail.linkpay.payment.service.PaymentService.createPayment(..))",
            throwing = "ex"
    )
    public void notifyOnPaymentFailure(final JoinPoint joinPoint, final Exception ex) {
        Object[] args = joinPoint.getArgs();

        TransactionInfo txInfo = (TransactionInfo) args[0];
        PaymentInfo payInfo = (PaymentInfo) args[1];

        paymentNotifier.notifyFailure(payInfo.memberId(), ex);
    }
}
