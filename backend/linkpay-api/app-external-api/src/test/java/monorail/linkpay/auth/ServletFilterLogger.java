package monorail.linkpay.auth;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.util.Map;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class ServletFilterLogger implements ServletContextInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        Map<String, ? extends FilterRegistration> filterRegistrations = servletContext.getFilterRegistrations();
        for (Map.Entry<String, ? extends FilterRegistration> entry : filterRegistrations.entrySet()) {
            System.out.println("Filter Name: " + entry.getKey() + ", Class: " + entry.getValue().getClassName());
        }
    }
}

