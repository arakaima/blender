package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.RiskAssessment;
import be.fgov.famhp.imt.gateway.service.dto.RiskAssessmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RiskAssessment} and its DTO {@link RiskAssessmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface RiskAssessmentMapper extends EntityMapper<RiskAssessmentDTO, RiskAssessment> {}
