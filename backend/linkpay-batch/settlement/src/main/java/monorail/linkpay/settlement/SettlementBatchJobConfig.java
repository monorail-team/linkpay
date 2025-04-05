package monorail.linkpay.settlement;

import monorail.linkpay.common.domain.outbox.Outbox;
import monorail.linkpay.event.Event;
import monorail.linkpay.event.payload.EventPayload;
import monorail.linkpay.outbox.OutboxEventWriter;
import monorail.linkpay.outbox.OutboxProcessor;
import monorail.linkpay.outbox.OutboxReader;
import monorail.linkpay.outbox.OutboxWriter;
import monorail.linkpay.payment.PaymentReader;
import monorail.linkpay.payment.domain.Payment;
import monorail.linkpay.walletHistory.WalletHistoryReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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
            final Step settlementStep,
            final Step publishOutboxStep
    ) {
        return new JobBuilder("settlementJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chargeStep)
                .next(settlementStep)
                .next(publishOutboxStep)
                .build();
    }

    @Bean
    @JobScope
    public Step chargeStep(
            final JobRepository jobRepository,
            final WalletHistoryReader walletHistoryReader,
            final OutboxWriter outboxWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("chargeStep", jobRepository)
                .<Payment, Payment>chunk(CHUNK_SIZE, transactionManager)
                .reader(walletHistoryReader.chargeItemReader())
                .writer(items -> {
                    outboxWriter.depositHistoryOutboxItemWriter();
                })
                .build();
    }

    @Bean
    @JobScope
    public Step settlementStep(
            final JobRepository jobRepository,
            final PaymentReader paymentReader,
            final OutboxWriter outboxWriter,
            final SettlementWriter settlementWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("settlementStep", jobRepository)
                .<Payment, Payment>chunk(CHUNK_SIZE, transactionManager)
                .reader(paymentReader.paymentItemReader())
                .writer(items -> {
                    settlementWriter.settlementItemWriter();
                    outboxWriter.paymentOutboxItemWriter();
                })
                .build();
    }

    @Bean
    @JobScope
    public Step publishOutboxStep(
            final JobRepository jobRepository,
            final OutboxReader outboxReader,
            final OutboxProcessor outboxProcessor,
            final OutboxEventWriter outboxEventWriter,
            final PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("publishOutboxStep", jobRepository)
                .<Outbox, Event<? extends EventPayload>>chunk(CHUNK_SIZE, transactionManager)
                .reader(outboxReader.outboxReader())
                .processor(outboxProcessor.outboxProcessor())
                .writer(outboxEventWriter.outboxEventWriter())
                .build();
    }
}
