package orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq;

/**
 * Interface for receiving messages.
 */
public interface MessageReceiver {
    /**
     * Receives and processes a message.
     * @param message the message to be processed.
     */
    void receiveMessage(String message);
}
