package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Inspector;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inspector} and its DTO {@link InspectorDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectorMapper extends EntityMapper<InspectorDTO, Inspector> {}
