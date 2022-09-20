package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Dossier.
 */
@Document(collection = "dossier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("dossier_number")
    private String dossierNumber;

    @Field("description")
    private String description;

    @Field("dossier_type")
    private String dossierType;

    @Field("dossier_status")
    private String dossierStatus;

    @Field("inspection_entity")
    private String inspectionEntity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Dossier id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDossierNumber() {
        return this.dossierNumber;
    }

    public Dossier dossierNumber(String dossierNumber) {
        this.setDossierNumber(dossierNumber);
        return this;
    }

    public void setDossierNumber(String dossierNumber) {
        this.dossierNumber = dossierNumber;
    }

    public String getDescription() {
        return this.description;
    }

    public Dossier description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDossierType() {
        return this.dossierType;
    }

    public Dossier dossierType(String dossierType) {
        this.setDossierType(dossierType);
        return this;
    }

    public void setDossierType(String dossierType) {
        this.dossierType = dossierType;
    }

    public String getDossierStatus() {
        return this.dossierStatus;
    }

    public Dossier dossierStatus(String dossierStatus) {
        this.setDossierStatus(dossierStatus);
        return this;
    }

    public void setDossierStatus(String dossierStatus) {
        this.dossierStatus = dossierStatus;
    }

    public String getInspectionEntity() {
        return this.inspectionEntity;
    }

    public Dossier inspectionEntity(String inspectionEntity) {
        this.setInspectionEntity(inspectionEntity);
        return this;
    }

    public void setInspectionEntity(String inspectionEntity) {
        this.inspectionEntity = inspectionEntity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", dossierNumber='" + getDossierNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", dossierType='" + getDossierType() + "'" +
            ", dossierStatus='" + getDossierStatus() + "'" +
            ", inspectionEntity='" + getInspectionEntity() + "'" +
            "}";
    }
}
