package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.InspectionType;
import be.fgov.famhp.imt.gateway.service.dto.InspectionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectionType} and its DTO {@link InspectionTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionTypeMapper extends EntityMapper<InspectionTypeDTO, InspectionType> {}
