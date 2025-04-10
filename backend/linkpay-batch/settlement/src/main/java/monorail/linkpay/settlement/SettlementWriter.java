package monorail.linkpay.settlement;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.settlement.domain.Settlement;
import monorail.linkpay.settlement.repository.SettlementRepository;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SettlementWriter {

    private final SettlementRepository settlementRepository;
    private final DataSource dataSource;
    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public ItemWriter<Payment> settlementItemWriter(final JdbcBatchItemWriter<Settlement> settlementWriter) {
        return chunk -> {
            Map<Long, Long> storeAmountMap = chunk.getItems().stream()
                    .collect(groupingBy(Payment::getStoreId, summingLong(Payment::getAmount)));

            List<Settlement> settlements = storeAmountMap.entrySet().stream()
                    .map(entry -> Settlement.builder()
                            .id(idGenerator.generate())
                            .storeId(entry.getKey())
                            .point(new Point(entry.getValue()))
                            .build())
                    .toList();

            settlementWriter.write(new Chunk<>(settlements));
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Settlement> jdbcSettlementItemWriter() {
        return new JdbcBatchItemWriterBuilder<Settlement>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO settlement (id, store_id, point) "
                        + "VALUES (:id, :storeId, :point)")
                .dataSource(dataSource)
                .build();
    }
}
