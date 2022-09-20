package be.fgov.famhp.imt.backoffice.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ContactPerson.
 */
@Document(collection = "contact_person")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("niss")
    private String niss;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @Field("language")
    private String language;

    @Field("email")
    private String email;

    @Field("phone_number")
    private String phoneNumber;

    @Field("role")
    private String role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ContactPerson id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNiss() {
        return this.niss;
    }

    public ContactPerson niss(String niss) {
        this.setNiss(niss);
        return this;
    }

    public void setNiss(String niss) {
        this.niss = niss;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public ContactPerson firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public ContactPerson lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLanguage() {
        return this.language;
    }

    public ContactPerson language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactPerson email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public ContactPerson phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return this.role;
    }

    public ContactPerson role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPerson)) {
            return false;
        }
        return id != null && id.equals(((ContactPerson) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactPerson{" +
            "id=" + getId() +
            ", niss='" + getNiss() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", language='" + getLanguage() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
