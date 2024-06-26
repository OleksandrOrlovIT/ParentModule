package orlov.oleksandr.programming.citycountryemailsender.service.schedule.impl;

import config.TestElasticsearchConfiguration;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import orlov.oleksandr.programming.citycountryemailsender.CityCountryEmailSenderApplication;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.model.SendStatus;
import orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.EmailMessageService;
import orlov.oleksandr.programming.citycountryemailsender.service.schedule.EmailScheduleService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {CityCountryEmailSenderApplication.class, TestElasticsearchConfiguration.class})
@EnableScheduling
class EmailScheduleServiceImplTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailMessageService emailMessageService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private EmailScheduleService emailScheduleService;

    @BeforeEach
    public void beforeEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).createMapping();
    }

    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).delete();
    }

    @Test
    void testSendEmailMessagesWithErrors() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setId("1");
        emailMessage.setEmail("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");
        emailMessage.setSendStatus(SendStatus.ERROR_WHILE_SENDING);
        emailMessage.setTryCount(1);
        emailMessage.setErrorMessage("Test Error");

        emailMessageService.save(emailMessage);

        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));

        emailScheduleService.sendEmailMessagesWithErrors();

        emailMessage = emailMessageService.findAll().get(0);

        assertEquals(SendStatus.SENT, emailMessage.getSendStatus());
        assertNull(emailMessage.getErrorMessage());
        assertEquals(2, emailMessage.getTryCount());
    }
}