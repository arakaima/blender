package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DossierStatus.
 */
@Document(collection = "dossier_status")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name_fr")
    private String nameFr;

    @Field("name_en")
    private String nameEn;

    @Field("name_nl")
    private String nameNl;

    @Field("name_de")
    private String nameDe;

    @Field("deprecated")
    private Boolean deprecated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public DossierStatus id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameFr() {
        return this.nameFr;
    }

    public DossierStatus nameFr(String nameFr) {
        this.setNameFr(nameFr);
        return this;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public DossierStatus nameEn(String nameEn) {
        this.setNameEn(nameEn);
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameNl() {
        return this.nameNl;
    }

    public DossierStatus nameNl(String nameNl) {
        this.setNameNl(nameNl);
        return this;
    }

    public void setNameNl(String nameNl) {
        this.nameNl = nameNl;
    }

    public String getNameDe() {
        return this.nameDe;
    }

    public DossierStatus nameDe(String nameDe) {
        this.setNameDe(nameDe);
        return this;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public Boolean getDeprecated() {
        return this.deprecated;
    }

    public DossierStatus deprecated(Boolean deprecated) {
        this.setDeprecated(deprecated);
        return this;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierStatus)) {
            return false;
        }
        return id != null && id.equals(((DossierStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierStatus{" +
            "id=" + getId() +
            ", nameFr='" + getNameFr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameNl='" + getNameNl() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", deprecated='" + getDeprecated() + "'" +
            "}";
    }
}
