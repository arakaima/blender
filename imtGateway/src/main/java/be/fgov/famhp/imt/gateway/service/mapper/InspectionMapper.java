package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Inspection;
import be.fgov.famhp.imt.gateway.service.dto.InspectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inspection} and its DTO {@link InspectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionMapper extends EntityMapper<InspectionDTO, Inspection> {}
