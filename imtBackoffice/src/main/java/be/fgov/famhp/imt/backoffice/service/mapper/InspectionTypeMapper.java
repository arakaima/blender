package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectionType} and its DTO {@link InspectionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionTypeMapper extends EntityMapper<InspectionTypeDTO, InspectionType> {}
