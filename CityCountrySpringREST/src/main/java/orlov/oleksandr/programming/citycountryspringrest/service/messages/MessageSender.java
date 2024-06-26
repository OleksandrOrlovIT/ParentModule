package orlov.oleksandr.programming.citycountryspringrest.service.messages;

/**
 * Interface to send messages in message broker
 */
public interface MessageSender {
    void sendMessageWithEmail(String message, String json);
}
