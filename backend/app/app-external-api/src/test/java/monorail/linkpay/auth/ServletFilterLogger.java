package monorail.linkpay.auth;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ServletFilterLogger implements ServletContextInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        System.out.println("===== Registered Servlet Filters =====");
        Map<String, ? extends FilterRegistration> filterRegistrations = servletContext.getFilterRegistrations();
        for (Map.Entry<String, ? extends FilterRegistration> entry : filterRegistrations.entrySet()) {
            System.out.println("Filter Name: " + entry.getKey() + ", Class: " + entry.getValue().getClassName());
        }
    }
}

