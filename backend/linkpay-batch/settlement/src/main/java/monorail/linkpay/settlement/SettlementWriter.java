package monorail.linkpay.settlement;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.Point;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.util.id.IdGenerator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SettlementWriter {

    private final SettlementRepository settlementRepository;
    private final IdGenerator idGenerator;

    @Bean
    @StepScope
    public ItemWriter<Payment> settlementItemWriter() {
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

            settlementRepository.saveAll(settlements);
        };
    }
}
