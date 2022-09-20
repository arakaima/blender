package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A InspectorDossier.
 */
@Document(collection = "inspector_dossier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InspectorDossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("inspector_id")
    private String inspectorId;

    @Field("dossier_id")
    private String dossierId;

    @Field("inspector_role")
    private String inspectorRole;

    @Field("expert_id")
    private String expertId;

    @Field("number_of_days")
    private Integer numberOfDays;

    @Field("inspector_employer")
    private String inspectorEmployer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public InspectorDossier id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectorId() {
        return this.inspectorId;
    }

    public InspectorDossier inspectorId(String inspectorId) {
        this.setInspectorId(inspectorId);
        return this;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getDossierId() {
        return this.dossierId;
    }

    public InspectorDossier dossierId(String dossierId) {
        this.setDossierId(dossierId);
        return this;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public String getInspectorRole() {
        return this.inspectorRole;
    }

    public InspectorDossier inspectorRole(String inspectorRole) {
        this.setInspectorRole(inspectorRole);
        return this;
    }

    public void setInspectorRole(String inspectorRole) {
        this.inspectorRole = inspectorRole;
    }

    public String getExpertId() {
        return this.expertId;
    }

    public InspectorDossier expertId(String expertId) {
        this.setExpertId(expertId);
        return this;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public Integer getNumberOfDays() {
        return this.numberOfDays;
    }

    public InspectorDossier numberOfDays(Integer numberOfDays) {
        this.setNumberOfDays(numberOfDays);
        return this;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getInspectorEmployer() {
        return this.inspectorEmployer;
    }

    public InspectorDossier inspectorEmployer(String inspectorEmployer) {
        this.setInspectorEmployer(inspectorEmployer);
        return this;
    }

    public void setInspectorEmployer(String inspectorEmployer) {
        this.inspectorEmployer = inspectorEmployer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InspectorDossier)) {
            return false;
        }
        return id != null && id.equals(((InspectorDossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InspectorDossier{" +
            "id=" + getId() +
            ", inspectorId='" + getInspectorId() + "'" +
            ", dossierId='" + getDossierId() + "'" +
            ", inspectorRole='" + getInspectorRole() + "'" +
            ", expertId='" + getExpertId() + "'" +
            ", numberOfDays=" + getNumberOfDays() +
            ", inspectorEmployer='" + getInspectorEmployer() + "'" +
            "}";
    }
}
