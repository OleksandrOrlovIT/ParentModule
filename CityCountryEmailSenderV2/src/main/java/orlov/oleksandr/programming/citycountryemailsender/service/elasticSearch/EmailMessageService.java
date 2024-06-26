package orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch;

import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;

import java.util.List;
import java.util.Optional;

/**
 * Service to work with EmailMessage entity
 */
public interface EmailMessageService {
    EmailMessage save(EmailMessage emailMessage);

    List<EmailMessage> findAllFailedMessages();

    List<EmailMessage> findAll();
}