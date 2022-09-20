package be.fgov.famhp.imt.backoffice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.fgov.famhp.imt.backoffice.domain.Inspector} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InspectorDTO implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InspectorDTO)) {
            return false;
        }

        InspectorDTO inspectorDTO = (InspectorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inspectorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InspectorDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
