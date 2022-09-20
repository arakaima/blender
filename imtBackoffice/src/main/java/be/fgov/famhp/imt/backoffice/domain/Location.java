package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Location.
 */
@Document(collection = "location")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("active_request")
    private String activeRequest;

    @Field("head_quarters")
    private Boolean headQuarters;

    @Field("status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Location id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActiveRequest() {
        return this.activeRequest;
    }

    public Location activeRequest(String activeRequest) {
        this.setActiveRequest(activeRequest);
        return this;
    }

    public void setActiveRequest(String activeRequest) {
        this.activeRequest = activeRequest;
    }

    public Boolean getHeadQuarters() {
        return this.headQuarters;
    }

    public Location headQuarters(Boolean headQuarters) {
        this.setHeadQuarters(headQuarters);
        return this;
    }

    public void setHeadQuarters(Boolean headQuarters) {
        this.headQuarters = headQuarters;
    }

    public String getStatus() {
        return this.status;
    }

    public Location status(String status) {
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
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", activeRequest='" + getActiveRequest() + "'" +
            ", headQuarters='" + getHeadQuarters() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
