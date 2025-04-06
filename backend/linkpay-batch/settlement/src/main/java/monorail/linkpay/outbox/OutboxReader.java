package monorail.linkpay.outbox;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.event.Outbox;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OutboxReader {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Outbox> outboxItemReader(
            @Value("#{jobParameters['before']}") final String beforeDateTime
    ) {
        LocalDateTime before = LocalDateTime.parse(beforeDateTime);
        JpaPagingItemReader<Outbox> reader = new JpaPagingItemReader<>();
        reader.setName("outboxReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(
                "SELECT outbox FROM Outbox outbox "
                        + "WHERE outbox.eventStatus = 'PENDING' "
                        + "AND outbox.createdAt < :before"
        );
        reader.setParameterValues(Map.of("before", before));
        reader.setPageSize(100);
        return reader;
    }
}
