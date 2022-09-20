package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Requests.
 */
@Document(collection = "requests")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Requests implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("inspection_id")
    private String inspectionId;

    @Field("title")
    private String title;

    @Field("datetime")
    private LocalDate datetime;

    @Field("status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Requests id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionId() {
        return this.inspectionId;
    }

    public Requests inspectionId(String inspectionId) {
        this.setInspectionId(inspectionId);
        return this;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getTitle() {
        return this.title;
    }

    public Requests title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDatetime() {
        return this.datetime;
    }

    public Requests datetime(LocalDate datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return this.status;
    }

    public Requests status(String status) {
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
        if (!(o instanceof Requests)) {
            return false;
        }
        return id != null && id.equals(((Requests) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Requests{" +
            "id=" + getId() +
            ", inspectionId='" + getInspectionId() + "'" +
            ", title='" + getTitle() + "'" +
            ", datetime='" + getDatetime() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
