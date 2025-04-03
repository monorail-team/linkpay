package monorail.linkpay.settlement.batch;

import lombok.RequiredArgsConstructor;
import monorail.linkpay.common.domain.outbox.OutboxEventConverter;
import monorail.linkpay.common.domain.outbox.OutboxRepository;
import monorail.linkpay.settlement.event.producer.EventPublisher;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxTasklet implements Tasklet {

    private final OutboxRepository outboxRepository;
    private final EventPublisher eventPublisher;

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ignored) {
        outboxRepository.findAll().stream()
                .map(OutboxEventConverter::toEvent)
                .forEach(eventPublisher::publish);
        return RepeatStatus.FINISHED;
    }
}
