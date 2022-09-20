package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A RequestsMessage.
 */
@Document(collection = "requests_message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("request_id")
    private String requestId;

    @Field("datetime")
    private LocalDate datetime;

    @Field("message")
    private String message;

    @Field("author")
    private String author;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public RequestsMessage id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public RequestsMessage requestId(String requestId) {
        this.setRequestId(requestId);
        return this;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public LocalDate getDatetime() {
        return this.datetime;
    }

    public RequestsMessage datetime(LocalDate datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public String getMessage() {
        return this.message;
    }

    public RequestsMessage message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return this.author;
    }

    public RequestsMessage author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestsMessage)) {
            return false;
        }
        return id != null && id.equals(((RequestsMessage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestsMessage{" +
            "id=" + getId() +
            ", requestId='" + getRequestId() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", message='" + getMessage() + "'" +
            ", author='" + getAuthor() + "'" +
            "}";
    }
}
