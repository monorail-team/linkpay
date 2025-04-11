package monorail.linkpay.outbox;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.event.Outbox;
import monorail.linkpay.common.event.OutboxRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Configuration
@RequiredArgsConstructor
public class OutboxWriter {

    private final OutboxRepository outboxRepository;
    private final DataSource dataSource;

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Outbox> jdbcOutboxItemWriter() {
        return new JdbcBatchItemWriterBuilder<Outbox>()
                .itemSqlParameterSourceProvider(outbox -> {
                    MapSqlParameterSource param = new MapSqlParameterSource();
                    param.addValue("id", outbox.getId());
                    param.addValue("eventType", outbox.getEventType().name());
                    param.addValue("payload", outbox.getPayload());
                    param.addValue("eventStatus", outbox.getEventStatus().name());
                    return param;
                })
                .sql("INSERT INTO outbox (outbox_id, event_type, payload, event_status) "
                        + "VALUES (:id, :eventType, :payload, :eventStatus)")
                .dataSource(dataSource)
                .build();
    }
}
