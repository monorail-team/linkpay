package monorail.linkpay.outbox;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.event.Outbox;
import monorail.linkpay.common.event.OutboxRepository;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OutboxWriter {

    private final OutboxRepository outboxRepository;
    private final DataSource dataSource;
    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Outbox> depositHistoryOutboxItemWriter() {
        return new JdbcBatchItemWriterBuilder<Outbox>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO outbox (id, event_type, payload, event_status) "
                        + "VALUES (:id, :eventType, :payload, :eventStatus)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Outbox> paymentOutboxItemWriter() {
        return new JdbcBatchItemWriterBuilder<Outbox>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO outbox (id, event_type, payload, event_status) "
                        + "VALUES (:id, :eventType, :payload, :eventStatus)")
                .dataSource(dataSource)
                .build();
    }
}
