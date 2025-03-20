package monorail.linkpay.common;

import monorail.linkpay.MockTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MockTestConfiguration.class)
@SpringBootTest
public abstract class IntegrationTest {
}
