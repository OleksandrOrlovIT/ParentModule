package orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.repository.EmailMessageRepository;
import orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.EmailMessageService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of service to work with EmailMessage Entity
 */
@AllArgsConstructor
@Service
public class EmailMessageServiceImpl implements EmailMessageService {

    private final EmailMessageRepository emailMessageRepository;


    /**
     * Method to save EmailMessage entity with Kiyv timezone
     * @param emailMessage
     * @return
     */
    @Override
    public EmailMessage save(EmailMessage emailMessage) {
        emailMessage.setLastTryDate(ZonedDateTime.now(ZoneId.of("Europe/Kiev")));
        return emailMessageRepository.save(emailMessage);
    }

    /**
     * Method that returns all EmailMessage document where ErrorMessage is not null
     * @return
     */
    @Override
    public List<EmailMessage> findAllFailedMessages() {
        return emailMessageRepository.findByErrorMessageIsNotNull();
    }

    /**
     * Method to findAll() EmailMessages
     * @return
     */
    @Override
    public List<EmailMessage> findAll() {
        List<EmailMessage> emailMessages = new ArrayList<>();

        for (EmailMessage emailMessage : emailMessageRepository.findAll()) {
            emailMessages.add(emailMessage);
        }

        return emailMessages;
    }
}