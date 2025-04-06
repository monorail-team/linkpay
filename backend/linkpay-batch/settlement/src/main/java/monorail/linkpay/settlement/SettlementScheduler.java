package monorail.linkpay.settlement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementScheduler {

    private final JobLauncher jobLauncher;
    private final Job settlementJob;

    @Scheduled(cron = "0 0 2 * * *")
    public void runSettlementJob() {
        try {
            LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay();
            LocalDateTime end = LocalDate.now().atStartOfDay();

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("start", start.toString())
                    .addString("end", end.toString())
                    .addString("before", end.toString())
                    .toJobParameters();

            jobLauncher.run(settlementJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
