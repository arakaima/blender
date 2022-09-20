package be.fgov.famhp.imt.backoffice.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.fgov.famhp.imt.backoffice.domain.InspectionType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InspectionTypeDTO implements Serializable {

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
        if (!(o instanceof InspectionTypeDTO)) {
            return false;
        }

        InspectionTypeDTO inspectionTypeDTO = (InspectionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inspectionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InspectionTypeDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
