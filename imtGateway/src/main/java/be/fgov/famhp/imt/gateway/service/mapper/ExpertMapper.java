package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Expert;
import be.fgov.famhp.imt.gateway.service.dto.ExpertDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Expert} and its DTO {@link ExpertDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpertMapper extends EntityMapper<ExpertDTO, Expert> {}
