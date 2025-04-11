package monorail.linkpay.settlement;

import static monorail.linkpay.BatchTestFixture.BEFORE;
import static monorail.linkpay.BatchTestFixture.END;
import static monorail.linkpay.BatchTestFixture.START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("local")
class SettlementBatchJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private Job chargeStep;
    @Autowired
    private Job settlementStep;
    @Autowired
    private Job publishOutboxStep;

    @Test
    void testChargeStep() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("start", String.valueOf(START))
                .addString("end", String.valueOf(END))
                .addLong("runId", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchStep("chargeStep", jobParameters);

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
    }

    @Test
    void testPaymentStep() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("start", String.valueOf(START))
                .addString("end", String.valueOf(END))
                .addLong("runId", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchStep("paymentStep", jobParameters);

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
    }

    @Test
    void testSettlementStep() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("start", String.valueOf(START))
                .addString("end", String.valueOf(END))
                .addLong("runId", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchStep("settlementStep", jobParameters);

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
    }

    @Test
    void testPublishOutboxStep() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("before", String.valueOf(BEFORE))
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchStep("publishOutboxStep", jobParameters);

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
    }
}