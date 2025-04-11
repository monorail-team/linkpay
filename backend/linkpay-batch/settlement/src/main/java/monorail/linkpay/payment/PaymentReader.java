package monorail.linkpay.payment;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.payment.domain.Payment;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PaymentReader {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Payment> paymentItemReader(
            @Value("#{jobParameters['start']}") final String startDateTime,
            @Value("#{jobParameters['end']}") final String endDateTime
    ) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        LocalDateTime end = LocalDateTime.parse(endDateTime);

        JpaPagingItemReader<Payment> reader = new JpaPagingItemReader<>();
        reader.setName("paymentItemReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT p FROM Payment p "
                        + "WHERE p.createdAt >= :start "
                        + "AND p.createdAt < :end"
        );
        reader.setParameterValues(Map.of("start", start, "end", end));
        reader.setPageSize(100);
        return reader;
    }
}
