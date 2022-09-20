package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import be.fgov.famhp.imt.backoffice.service.dto.RiskAssessmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RiskAssessment} and its DTO {@link RiskAssessmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface RiskAssessmentMapper extends EntityMapper<RiskAssessmentDTO, RiskAssessment> {}
