package monorail.linkpay.settlement;

import monorail.linkpay.common.event.Outbox;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.history.domain.WalletHistory;
import monorail.linkpay.payment.domain.Payment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SettlementBatchJobConfig {

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job settlementJob(
            final JobRepository jobRepository,
            final Step chargeStep,
            final Step paymentStep,
            final Step settlementStep,
            final Step publishOutboxStep
    ) {
        return new JobBuilder("settlementJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chargeStep)
                .next(paymentStep)
                .next(settlementStep)
                .next(publishOutboxStep)
                .build();
    }

    @Bean
    @JobScope
    public Step chargeStep(
            final JobRepository jobRepository,
            final JpaPagingItemReader<WalletHistory> chargeReader,
            final ItemProcessor<WalletHistory, Outbox> walletHistoryProcessor,
            final JdbcBatchItemWriter<Outbox> outboxWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("chargeStep", jobRepository)
                .<WalletHistory, Outbox>chunk(CHUNK_SIZE, transactionManager)
                .reader(chargeReader)
                .processor(walletHistoryProcessor)
                .writer(outboxWriter)
                .build();
    }

    @Bean
    @JobScope
    public Step paymentStep(
            final JobRepository jobRepository,
            final JpaPagingItemReader<Payment> paymentReader,
            final ItemProcessor<Payment, Outbox> paymentProcessor,
            final JdbcBatchItemWriter<Outbox> outboxWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("paymentStep", jobRepository)
                .<Payment, Outbox>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader)
                .processor(paymentProcessor)
                .writer(outboxWriter)
                .build();
    }

    @Bean
    @JobScope
    public Step settlementStep(
            final JobRepository jobRepository,
            final JpaPagingItemReader<Payment> paymentReader,
            final ItemWriter<Payment> settlementItemWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("settlementStep", jobRepository)
                .<Payment, Payment>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader)
                .writer(settlementItemWriter)
                .build();
    }

    @Bean
    @JobScope
    public Step publishOutboxStep(
            final JobRepository jobRepository,
            final JpaPagingItemReader<Outbox> outboxReader,
            final ItemProcessor<Outbox, Event<EventPayload>> outboxProcessor,
            final ItemWriter<Event<? extends EventPayload>> outboxEventWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("publishOutboxStep", jobRepository)
                .<Outbox, Event<? extends EventPayload>>chunk(CHUNK_SIZE, transactionManager)
                .reader(outboxReader)
                .processor(outboxProcessor)
                .writer(outboxEventWriter)
                .build();
    }
}
