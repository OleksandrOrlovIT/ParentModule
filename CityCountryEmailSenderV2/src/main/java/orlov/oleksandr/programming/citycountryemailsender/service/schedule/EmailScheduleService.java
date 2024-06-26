package orlov.oleksandr.programming.citycountryemailsender.service.schedule;

/**
 * Service interface for scheduling email message sending.
 */
public interface EmailScheduleService {
    /**
     * Sends email messages that previously encountered errors.
     */
    void sendEmailMessagesWithErrors();
}
