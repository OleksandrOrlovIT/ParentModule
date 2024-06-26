package orlov.oleksandr.programming.citycountryemailsender.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * EmailMessage entity document for elasticsearch
 */
@NoArgsConstructor
@Setter
@Getter
@Document(indexName = "email_messages")
public class EmailMessage {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String email;

    @Field(type = FieldType.Text)
    private SendStatus sendStatus;

    @Field(type = FieldType.Integer)
    private Integer tryCount;

    @Field(type = FieldType.Date)
    private ZonedDateTime lastTryDate;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Builder
    public EmailMessage(String id, String subject, String content, String email, SendStatus sendStatus, Integer tryCount, ZonedDateTime lastTryDate, String errorMessage) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.email = email;
        this.sendStatus = sendStatus;
        this.tryCount = tryCount;
        this.lastTryDate = lastTryDate;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailMessage that = (EmailMessage) o;
        return Objects.equals(id, that.id) && Objects.equals(subject, that.subject) && Objects.equals(content, that.content) && Objects.equals(email, that.email) && sendStatus == that.sendStatus && Objects.equals(tryCount, that.tryCount) && Objects.equals(lastTryDate, that.lastTryDate) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(subject);
        result = 31 * result + Objects.hashCode(content);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(sendStatus);
        result = 31 * result + Objects.hashCode(tryCount);
        result = 31 * result + Objects.hashCode(lastTryDate);
        result = 31 * result + Objects.hashCode(errorMessage);
        return result;
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", email='" + email + '\'' +
                ", sendStatus=" + sendStatus +
                ", tryCount=" + tryCount +
                ", lastTryDate=" + lastTryDate +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}