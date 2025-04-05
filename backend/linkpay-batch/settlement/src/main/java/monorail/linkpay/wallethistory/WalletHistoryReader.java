package monorail.linkpay.wallethistory;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.payment.domain.Payment;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WalletHistoryReader {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Payment> chargeItemReader() {
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();

        JpaPagingItemReader<Payment> reader = new JpaPagingItemReader<>();
        reader.setName("chargeItemReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT wh FROM WalletHistory wh "
                        + "WHERE wh.createdAt >= :start "
                        + "AND wh.createdAt < :end"
                        + "AND wh.transactionType = 'DEPOSIT'"
        );
        reader.setParameterValues(Map.of("start", start, "end", end));
        reader.setPageSize(100);
        return reader;
    }
}
