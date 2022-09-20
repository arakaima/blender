package be.fgov.famhp.imt.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link be.fgov.famhp.imt.gateway.domain.RiskAssessment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RiskAssessmentDTO implements Serializable {

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
        if (!(o instanceof RiskAssessmentDTO)) {
            return false;
        }

        RiskAssessmentDTO riskAssessmentDTO = (RiskAssessmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, riskAssessmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RiskAssessmentDTO{" +
            "id='" + getId() + "'" +
            "}";
    }
}
