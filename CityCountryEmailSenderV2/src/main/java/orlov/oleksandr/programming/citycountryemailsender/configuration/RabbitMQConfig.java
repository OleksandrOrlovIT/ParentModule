package orlov.oleksandr.programming.citycountryemailsender.configuration;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq.MessageReceiver;

/**
 * Configuration class for RabbitMQ setup.
 */
@AllArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final Dotenv dotenv;

    /**
     * Provides the ConnectionFactory bean configured with RabbitMQ server details.
     * @return ConnectionFactory object configured with host, port, username, and password.
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
     * Provides a queue bean named "myQueue".
     * @return Queue object named "myQueue".
     */
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }

    /**
     * Provides a SimpleMessageListenerContainer bean configured with a message listener.
     * @param connectionFactory The ConnectionFactory bean.
     * @param listenerAdapter The MessageListenerAdapter bean.
     * @return SimpleMessageListenerContainer object configured with connection factory and queue names.
     */
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("myQueue");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /**
     * Provides a MessageListenerAdapter bean configured with a message receiver.
     * @param receiver The MessageReceiver bean.
     * @return MessageListenerAdapter object configured with the receiver and method name.
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}