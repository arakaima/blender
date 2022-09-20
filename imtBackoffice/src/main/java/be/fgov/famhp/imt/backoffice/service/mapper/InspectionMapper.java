package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Inspection;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inspection} and its DTO {@link InspectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionMapper extends EntityMapper<InspectionDTO, Inspection> {}
