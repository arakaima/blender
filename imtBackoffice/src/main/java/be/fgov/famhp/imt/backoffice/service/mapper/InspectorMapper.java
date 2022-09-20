package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Inspector;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inspector} and its DTO {@link InspectorDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectorMapper extends EntityMapper<InspectorDTO, Inspector> {}
