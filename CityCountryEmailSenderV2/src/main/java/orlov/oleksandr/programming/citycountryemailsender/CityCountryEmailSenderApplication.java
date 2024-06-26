package orlov.oleksandr.programming.citycountryemailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Start point of the application
 */
@SpringBootApplication
public class CityCountryEmailSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityCountryEmailSenderApplication.class, args);
    }
}
