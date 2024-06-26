package orlov.oleksandr.programming.citycountryspringrest.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ setup.
 */
@AllArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final Dotenv dotenv;

    /**
     * Bean for configuring RabbitMQ connection factory.
     *
     * @return RabbitMQ connection factory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        String host = dotenv.get("RABBITMQ_HOST");
        int port = Integer.parseInt(dotenv.get("RABBITMQ_PORT"));
        String username = dotenv.get("RABBITMQ_USERNAME");
        String password = dotenv.get("RABBITMQ_PASSWORD");

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    /**
     * Bean for configuring RabbitMQ template.
     *
     * @return RabbitMQ template
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    /**
     * Bean for defining a queue.
     *
     * @return Queue instance
     */
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }

    /**
     * Bean for configuring RabbitMQ admin.
     *
     * @return RabbitMQ admin
     */
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}
