package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Note.
 */
@Document(collection = "note")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("note_number")
    private String noteNumber;

    @Field("send_date")
    private LocalDate sendDate;

    @Field("label")
    private String label;

    @Field("status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Note id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteNumber() {
        return this.noteNumber;
    }

    public Note noteNumber(String noteNumber) {
        this.setNoteNumber(noteNumber);
        return this;
    }

    public void setNoteNumber(String noteNumber) {
        this.noteNumber = noteNumber;
    }

    public LocalDate getSendDate() {
        return this.sendDate;
    }

    public Note sendDate(LocalDate sendDate) {
        this.setSendDate(sendDate);
        return this;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }

    public String getLabel() {
        return this.label;
    }

    public Note label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return this.status;
    }

    public Note status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", noteNumber='" + getNoteNumber() + "'" +
            ", sendDate='" + getSendDate() + "'" +
            ", label='" + getLabel() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
