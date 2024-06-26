package orlov.oleksandr.programming.citycountryemailsender.service.email.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import orlov.oleksandr.programming.citycountryemailsender.model.EmailMessage;
import orlov.oleksandr.programming.citycountryemailsender.model.SendStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomEmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private CustomEmailService customEmailService;

    @Test
    public void testSendEmailMessage_Success() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmail("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        EmailMessage result = customEmailService.sendEmailMessage(emailMessage);

        assertEquals(SendStatus.SENT, result.getSendStatus());
        assertNull(result.getErrorMessage());
        assertEquals(1, result.getTryCount());

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("Test Content", sentMessage.getText());
    }

    @Test
    public void testSendEmailMessage_Failure() {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setEmail("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        doThrow(new RuntimeException("Mail server not available")).when(emailSender).send(any(SimpleMailMessage.class));

        EmailMessage result = customEmailService.sendEmailMessage(emailMessage);

        assertEquals(SendStatus.ERROR_WHILE_SENDING, result.getSendStatus());
        assertNotNull(result.getErrorMessage());
        assertEquals(1, result.getTryCount());

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(captor.capture());
        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@example.com", sentMessage.getTo()[0]);
        assertEquals("Test Subject", sentMessage.getSubject());
        assertEquals("Test Content", sentMessage.getText());
    }
}