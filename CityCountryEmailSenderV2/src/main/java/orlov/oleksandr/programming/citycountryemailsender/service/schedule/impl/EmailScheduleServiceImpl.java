package orlov.oleksandr.programming.citycountryemailsender.service.schedule.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.EmailMessageService;
import orlov.oleksandr.programming.citycountryemailsender.service.email.EmailService;
import orlov.oleksandr.programming.citycountryemailsender.service.schedule.EmailScheduleService;

/**
 * Implementation of the EmailScheduleService interface for scheduling email message sending.
 */
@Slf4j
@AllArgsConstructor
@Service
public class EmailScheduleServiceImpl implements EmailScheduleService {

    private final EmailService emailService;
    private final EmailMessageService emailMessageService;

    /**
     * Periodically sends email messages that previously encountered errors.
     * This method is scheduled to run with a fixed delay of 300,000 milliseconds (5 minutes).
     */
    @Scheduled(fixedDelay = 300000)
    @Override
    public void sendEmailMessagesWithErrors() {
        for(EmailMessage emailMessage : emailMessageService.findAllFailedMessages()){
            EmailMessage sentEmailMessage = emailService.sendEmailMessage(emailMessage);

            EmailMessage savedEmailMessage = emailMessageService.save(sentEmailMessage);

            log.info("Saved email message {}", savedEmailMessage);
        }
    }
}