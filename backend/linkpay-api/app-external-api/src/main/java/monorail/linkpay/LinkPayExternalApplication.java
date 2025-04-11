package monorail.linkpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class LinkPayExternalApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinkPayExternalApplication.class, args);
    }
}
