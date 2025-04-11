package monorail.linkpay.settlement;

import static monorail.linkpay.BatchTestFixture.BEFORE;
import static monorail.linkpay.BatchTestFixture.END;
import static monorail.linkpay.BatchTestFixture.START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.batch.core.ExitStatus.COMPLETED;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
class SettlementSchedulerTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private Job settlementJob;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(settlementJob);
    }

    @Test
    void testJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("start", String.valueOf(START))
                .addString("end", String.valueOf(END))
                .addString("before", String.valueOf(BEFORE))
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(execution.getExitStatus()).isEqualTo(COMPLETED);
    }
}