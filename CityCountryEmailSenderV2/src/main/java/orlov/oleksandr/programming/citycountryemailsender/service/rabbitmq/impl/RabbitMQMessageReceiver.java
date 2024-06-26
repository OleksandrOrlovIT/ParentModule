package orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.EmailMessageService;
import orlov.oleksandr.programming.citycountryemailsender.service.email.EmailService;
import orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq.MessageReceiver;

import java.util.Map;

/**
 * Service class for receiving and processing RabbitMQ messages.
 */
@Slf4j
@AllArgsConstructor
@Service
public class RabbitMQMessageReceiver implements MessageReceiver {

    private final EmailMessageService emailMessageService;
    private final EmailService emailService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Receives and processes a message from RabbitMQ.
     * Converts the message to an EmailMessage object, saves it,
     * attempts to send the email, and saves the updated email message status.
     * @param message the message to be processed.
     */
    public void receiveMessage(String message) {
        try {
            Map<String, String> messageContent = objectMapper.readValue(message, Map.class);

            EmailMessage emailMessage = EmailMessage.builder()
                    .subject(messageContent.get("subject"))
                    .content(messageContent.get("content"))
                    .email(messageContent.get("email"))
                    .build();

            EmailMessage savedEmailMessage = emailMessageService.save(emailMessage);
            log.info("Saved email message {}", savedEmailMessage);

            EmailMessage sentEmailMessage = emailService.sendEmailMessage(savedEmailMessage);
            savedEmailMessage = emailMessageService.save(sentEmailMessage);
            log.info("Saved email after trying to send it message {}", savedEmailMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse message", e);
        }
    }
}