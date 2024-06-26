package orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestElasticsearchConfiguration;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import orlov.oleksandr.programming.citycountryemailsender.CityCountryEmailSenderApplication;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.service.elasticSearch.EmailMessageService;
import orlov.oleksandr.programming.citycountryemailsender.service.rabbitmq.MessageReceiver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {CityCountryEmailSenderApplication.class, TestElasticsearchConfiguration.class})
class RabbitMQMessageReceiverTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private MessageReceiver messageReceiver;

    @Autowired
    private EmailMessageService emailMessageService;

    @MockBean
    private JavaMailSender javaMailSender;

    private static Map<String, String> messages;

    private static String message;

    @BeforeAll
    public static void beforeAll() throws JsonProcessingException {
        messages = new HashMap<>();

        messages.put("subject", "subject");
        messages.put("content", "content");
        messages.put("email", "email");

        ObjectMapper objectMapper = new ObjectMapper();
        message = objectMapper.writeValueAsString(messages);
    }

    @BeforeEach
    public void beforeEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).createMapping();
    }

    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).delete();
    }

    @Test
    void receiveMessage_WithoutError() {
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));

        messageReceiver.receiveMessage(message);

        List<EmailMessage> emailMessages = emailMessageService.findAll();
        EmailMessage savedEmailMessage = emailMessages.get(0);

        assertEquals(1, emailMessages.size());
        assertEquals(messages.get("subject"), savedEmailMessage.getSubject());
        assertEquals(messages.get("content"), savedEmailMessage.getContent());
        assertEquals(messages.get("email"), savedEmailMessage.getEmail());
        assertEquals(1, savedEmailMessage.getTryCount());
        assertNotNull(savedEmailMessage.getId());
        assertNotNull(savedEmailMessage.getLastTryDate());
        assertNull(savedEmailMessage.getErrorMessage());
    }

    @Test
    void receiveMessage_WithError() {
        doThrow(new RuntimeException("Mail server not available")).when(javaMailSender).send(any(SimpleMailMessage.class));

        messageReceiver.receiveMessage(message);

        List<EmailMessage> emailMessages = emailMessageService.findAllFailedMessages();
        EmailMessage savedEmailMessage = emailMessages.get(0);

        assertEquals(1, emailMessages.size());
        assertEquals(messages.get("subject"), savedEmailMessage.getSubject());
        assertEquals(messages.get("content"), savedEmailMessage.getContent());
        assertEquals(messages.get("email"), savedEmailMessage.getEmail());
        assertEquals(1, savedEmailMessage.getTryCount());
        assertNotNull(savedEmailMessage.getId());
        assertNotNull(savedEmailMessage.getLastTryDate());
        assertNotNull(savedEmailMessage.getErrorMessage());
    }
}