package orlov.oleksandr.programming.citycountryemailsender.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for loading environment variables.
 */
@Configuration
public class EnvConfig {

    /**
     * Provides the Dotenv object loaded with environment variables.
     * @return Dotenv object loaded with environment variables.
     */
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}
