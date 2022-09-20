package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A DossierType.
 */
@Document(collection = "dossier_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DossierType implements Serializable {

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

    public DossierType id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameFr() {
        return this.nameFr;
    }

    public DossierType nameFr(String nameFr) {
        this.setNameFr(nameFr);
        return this;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public DossierType nameEn(String nameEn) {
        this.setNameEn(nameEn);
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameNl() {
        return this.nameNl;
    }

    public DossierType nameNl(String nameNl) {
        this.setNameNl(nameNl);
        return this;
    }

    public void setNameNl(String nameNl) {
        this.nameNl = nameNl;
    }

    public String getNameDe() {
        return this.nameDe;
    }

    public DossierType nameDe(String nameDe) {
        this.setNameDe(nameDe);
        return this;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public Boolean getDeprecated() {
        return this.deprecated;
    }

    public DossierType deprecated(Boolean deprecated) {
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
        if (!(o instanceof DossierType)) {
            return false;
        }
        return id != null && id.equals(((DossierType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierType{" +
            "id=" + getId() +
            ", nameFr='" + getNameFr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameNl='" + getNameNl() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", deprecated='" + getDeprecated() + "'" +
            "}";
    }
}
