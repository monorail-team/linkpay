package monorail.linkpay.wallethistory;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.history.domain.WalletHistory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WalletHistoryReader {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<WalletHistory> chargeItemReader(
            @Value("#{jobParameters['start']}") final String startDateTime,
            @Value("#{jobParameters['end']}") final String endDateTime
    ) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        LocalDateTime end = LocalDateTime.parse(endDateTime);

        JpaPagingItemReader<WalletHistory> reader = new JpaPagingItemReader<>();
        reader.setName("chargeItemReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT wh FROM WalletHistory wh "
                        + "WHERE wh.createdAt >= :start "
                        + "AND wh.createdAt < :end "
                        + "AND wh.transactionType = 'DEPOSIT'"
        );
        reader.setParameterValues(Map.of("start", start, "end", end));
        reader.setPageSize(100);
        return reader;
    }
}
