package monorail.linkpay.settlement;

import java.util.List;
import monorail.linkpay.payment.domain.Payment;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SettlementCompositeWriterConfig {

    @Bean
    @StepScope
    public CompositeItemWriter<Payment> settlementCompositeWriter(
            @Qualifier("paymentOutboxItemWriter") final ItemWriter<Payment> outboxWriter,
            @Qualifier("settlementItemWriter") final ItemWriter<Payment> settlementWriter
    ) {
        CompositeItemWriter<Payment> writer = new CompositeItemWriter<>();
        writer.setDelegates(List.of(outboxWriter, settlementWriter));
        return writer;
    }
}
